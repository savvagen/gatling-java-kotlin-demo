package com.example.protocols.helloworld.protocol

class HelloWorld(funcName: String){

  def sendMessage(message: String) = new HelloWorldMessageActionBuilder(funcName, message)

}

