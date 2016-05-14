package com.Pineapple.Dao.model;
import java.io.Serializable;

public class Bag implements Serializable{
	private static final long serialVersionUID = 1L;
	private int ID;
	private String clientID;
	private String computerID;
	private int number;
	private double price;
	private String color;
	private String size;
	private String stock;
	private String memory;
	private String graphics;
	private String processor;
	
	public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    
    public String getclientID() {
        return clientID;
    }
    public void setclientID(String clientID) {
        this.clientID = clientID;
    }
    public String getcomputerID() {
        return computerID;
    }
    public void setcomputerID(String computerID) {
        this.computerID = computerID;
    }
    public int getnumber() {
        return number;
    }
    public void setnumber(int number) {
        this.number = number;
    }
    public double getprice() {
        return price;
    }
    public void setprice(double price) {
        this.price = price;
    }
    public String getcolor() {
        return color;
    }
    public void setcolor(String color) {
        this.color = color;
    }
    public String getsize() {
        return size;
    }
    public void setsize(String size) {
        this.size = size;
    }
    public String getstock() {
        return stock;
    }
    public void setstock(String stock) {
        this.stock = stock;
    }
    public String getmemory() {
        return memory;
    }
    public void setmemory(String memory) {
        this.memory = memory;
    }
    public String getgraphics() {
        return graphics;
    }
    public void setgraphics(String graphics) {
        this.graphics = graphics;
    }
    public String getprocessor() {
        return processor;
    }
    public void setprocessor(String processor) {
        this.processor = processor;
    }

}

