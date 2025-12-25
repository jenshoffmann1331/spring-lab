package org.example;

public class Alien {

  private int age;
  private Computer computer;

  public Alien() {
    System.out.println("Alien object created");
  }

  public void code() {
    System.out.println("Coding");
    computer.compile();
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    System.out.println("setAge called");
    this.age = age;
  }

  public void setLaptop(Computer computer) {
    this.computer = computer;
  }
}
