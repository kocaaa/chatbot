from keras.layers import Dense, Dropout
from nltk.stem import WordNetLemmatizer
from keras.models import Sequential

import tensorflow as tf
import numpy as np
import pickle
import random
import json
import nltk

def train():
    nltk.download('punkt')
    nltk.download('wordnet')
    
    lemmatizer = WordNetLemmatizer()
    intents = json.loads(open('intents.json', encoding="utf8").read())
    words = []
    classes = []
    documents = []
    ignore_letters = ['?', '!', '.', ',']
    
    for intent in intents["intents"]:
        for pattern in intent['patterns']:
            word_list = nltk.word_tokenize(pattern)
            words.extend(word_list)
            documents.append((word_list, intent['tag']))
            if intent['tag'] not in classes:
                classes.append(intent['tag'])
                
    words = [lemmatizer.lemmatize(word) for word in words if word not in ignore_letters]
    
    words = sorted(set(words))
    classes = sorted(set(classes))
    
    pickle.dump(words, open('words.pkl', 'wb'))
    pickle.dump(classes, open('classes.pkl', 'wb'))
    
    training = []
    output_empty = [0] * len(classes)
    
    for document in documents:
        bag = []
        word_patterns = document[0]
        word_patterns = [lemmatizer.lemmatize(word.lower()) for word in word_patterns]
        for word in words:
            bag.append(1) if word in word_patterns else bag.append(0)
            
        output_row = list(output_empty)
        output_row[classes.index(document[1])] = 1
        
        training.append([bag, output_row])
    
    random.shuffle(training)
    
    training = np.array(training, dtype=object)
    
    train_x = list(training[:, 0])
    train_y = list(training[:, 1])
    
    model = Sequential()
    model.add(Dense(128, input_shape=(len(train_x[0]),), activation='relu'))
    model.add(Dropout(0.5))
    model.add(Dense(64, activation='relu'))
    model.add(Dropout(0.5))
    model.add(Dense(len(train_y[0]), activation='softmax'))
    
    learning_rate = tf.keras.optimizers.schedules.ExponentialDecay(
        initial_learning_rate=0.01,
        decay_steps=10000,
        decay_rate=0.9)
    optimizer = tf.keras.optimizers.SGD(learning_rate=learning_rate, momentum=0.9, nesterov=True)
    model.compile(loss='categorical_crossentropy', optimizer=optimizer, metrics=['accuracy'])
    
    hist = model.fit(np.array(train_x), np.array(train_y), epochs=200, batch_size=5, verbose=1)
    model.save('chatbot_model.h5', hist)
    
    print("Model successfully created.")
