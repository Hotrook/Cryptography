# AES encryption app

This is s simple encryption app using AES cipher. It lets encrypting in OFB, CTR and CBC mode. App works in tho modes:

- Challenge - 
  it takes on input two messages, chooses randomly one of them and encrypts it.
- Encryption oracle -
  takes on input paths to files and configuration and then encrypts given files. For a file
  with a name `{filename}` it saves the results in `{filename}.dec`.
  
# CPA distinguisher 

There is also a demonstration with simple Chosen Plaintext Attack, which works under condition 
that the IV increments its value by 1, every time it is run.

## Before run

To run this app you need generated AES key. Key should be stored in `src/main/resources/keystore.jceks` file.
If you want to know how to generate the key you can check that [here](https://dzone.com/articles/aes-256-encryption-java-and).

The password to the generated key should be saved in `src/main/resources/password.txt` file.

Now you have necessary setup to run the app.

## How to run 

You can run AES encryption app simply by typing:
```console
foo@bar:~$ mvn clean install
foo@bar:~$ mvn exec:java
``` 

You run CPA distinguisher by typing:
```console
foo@bar:~$ mvn clean install
foo@bar:~$ mvn exec:java@CPA
``` 