package clueless;

/*
* Borrowed from https://www.javatpoint.com/Puzzle-Game 
* Adapted to only have buttons
*/

import java.awt.*;  
import javax.swing.*;  
import java.awt.event.*;  
public class GUI extends JFrame{
JButton b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12,b13,b14,b15,b16,b17,b18,b19,b20,b21;
GUI(){  
super("GUI");  
 b1=new JButton("Study");  
 b2=new JButton("Hallway");  
 b3=new JButton("Hall");  
 b4=new JButton("Hallway");  
 b5=new JButton("Lounge");  
 b6=new JButton("Hallway");  
 b7=new JButton("Hallway");  
 b8=new JButton("Hallway");  
 b9=new JButton("Library");
 b10=new JButton("Hallway");  
 b11=new JButton("Billiard Room");  
 b12=new JButton("Hallway");  
 b13=new JButton("Dining Room");  
 b14=new JButton("Hallway");  
 b15=new JButton("Hallway");  
 b16=new JButton("Hallway");  
 b17=new JButton("Conservatory");  
 b18=new JButton("Hallway");
 b19=new JButton("Ballroom");  
 b20=new JButton("Hallway");  
 b21=new JButton("Kitchen");    
  
b1.setBounds(0,0,150,150);  
b2.setBounds(150,50,150,50);  
b3.setBounds(300,0,150,150);  
b4.setBounds(450,50,150,50);  
b5.setBounds(600,0,150,150); 
 
b6.setBounds(50,150,50,150);  
b7.setBounds(350,150,50,150);  
b8.setBounds(650,150,50,150);  

b9.setBounds(0,300,150,150);  
b10.setBounds(150, 350, 150, 50);  
b11.setBounds(300, 300, 150, 150);
b12.setBounds(450, 350, 150, 50);
b13.setBounds(600, 300, 150, 150);

b14.setBounds(50,450,50,150);  
b15.setBounds(350,450,50,150);  
b16.setBounds(650,450,50,150); 

b17.setBounds(0,600,150,150);  
b18.setBounds(150, 650, 150, 50);  
b19.setBounds(300, 600, 150, 150);
b20.setBounds(450, 650, 150, 50);
b21.setBounds(600, 600, 150, 150);

    
add(b1);add(b2);add(b3);add(b4);add(b5);add(b6);add(b7);add(b8);add(b9);add(b10);add(b11); 
add(b12);add(b13);add(b14);add(b15);add(b16);add(b17);add(b18);add(b19);add(b20);add(b21);

setSize(900,900);  
setLayout(null);  
setVisible(true);  
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
}
  
public static void main(String[] args){  
new GUI();  
}//end of main  
  
}//end of class  