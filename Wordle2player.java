package Wordle;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Wordle2player {
    static int GREEN = 1, YELLOW=2, BLACK=3, RED=4;
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31;9m";


    static String[] validWords;

    public static void main(String args[]){
        //greetings
        System.out.println("MUHAHAHAH YOU WILL GET ADDICTED AND PAY ME UR MONEY, and leave a good review.");
        //pick the answer
        setup();
        //start play
        while(true){
            play();
        }



    }
    //read file, get word
    static void setup(){
        validWords=new String[2315];
        try {
            Scanner sc = new Scanner(new FileReader("valid_answers.txt"));
            for(int i = 0; i<validWords.length; i++){
                String aWord=sc.nextLine();
                validWords[i] = aWord;
            }
        } catch (IOException ioe){
            System.out.println("Error met in reading file" + ioe.getMessage());
        }

    }
    //plays the functions to the game over and over.
    static String key;
    static ArrayList <String> Guesses;
    static int[] alphabetColor;

    static void play(){
        System.out.println("Enter your key or click enter to get a random key.");
        Scanner sc = new Scanner (System.in);
        Guesses=new ArrayList<>();
        alphabetColor=new int[26];
        String choice = sc.nextLine();
        if(choice.isEmpty()){
            key = createKey();
        } else{
            if(choice.length()==5)
                key=choice;
            else{
                System.out.println("NOT A VALID WORD");
                return;
            }
        }
//        key="ababc";
//
//        System.out.println(key);
        for(int i =0; i<100;i++){
            System.out.println();
        }
        for(int i =0; i<6; i++){
            System.out.print("enter your guess #"+i+" :");
            String guess ="";
            while(true) {
                guess = sc.nextLine();
                if(isValid(guess)){
                    break;
                }

                System.out.println("try again");
            }
            processGuess(guess);
            for(String eachGuess: Guesses){
                System.out.println(eachGuess);

            }
//display alphabet with color
            for(int j = 0; j<26; j++){
                if(alphabetColor[j]==GREEN){
                    System.out.print(ANSI_GREEN);
                }
                else if(alphabetColor[j]==YELLOW){
                    System.out.print(ANSI_YELLOW);
                }
                else if(alphabetColor[j]==RED){
                    System.out.print(ANSI_RED);
                }
                else{
                    System.out.print(ANSI_RESET);
                }
                System.out.print((char)('a'+j) + ANSI_RESET+ " ");
            }
            System.out.print(ANSI_RESET);
            System.out.println();


            if(guess.equals(key)){
                System.out.println("You got it!!");
                return;
            }

        }
        System.out.println("SAD, answer was :" + key);

    }
    static boolean isValid(String guess){

        if (guess.length()!=5) return false;
        for(String Word:validWords){
            if(Word.equals(guess)){
                return true;
            }
        }
        return false;
    }

    static void processGuess(String guess) {
        int[] color = new int[guess.length()];
        //figure out the right color to use to display for this current guess
        //check each position, look at the char in the guess,
        // 1) if the key has the same char at this position, it is green
        // 2) else if the char doesn't exist in the key, then it is black
        // 3) else make it yellow (FOR NOW, will enhance this at a later time)
        for (int i = 0; i < guess.length(); i++) {
            char c = guess.charAt(i); //the guess character
            if (c == key.charAt(i)) { //case #1 (green)
                color[i] = GREEN;
                alphabetColor[c-'a']=GREEN;
            }
            else if (key.indexOf(c) == -1) {
                color[i] = BLACK;
                alphabetColor[c-'a']=RED;
            }
//            else //complicated case
//                color[i] = YELLOW; //flawed, will enhance, it could also be black
        }
        //second pass, only non-green or non-black
        for(int i =0; i<guess.length();i++){
            if(color[i]!=0)continue;
            char c = guess.charAt(i);
            int count = 0;
            for(int j = 0; j<key.length(); j++){
                if(key.charAt(j)==c && color[j]!=GREEN)
                    count++;
            }
            int countSeqInGuess = 0;
            for (int j = 0; j <= i; j++) {
                if (guess.charAt(j)==c && color[j]!=GREEN)
                    countSeqInGuess++;
            }
            if (countSeqInGuess>count) {
                color[i] = BLACK;
                alphabetColor[c-'a']=RED;
            }
            else {
                color[i] = YELLOW;
                alphabetColor[c-'a']=YELLOW;
            }


        }
        String guessWithColor="";
        for(int i =0; i<guess.length(); i++){
            if(color[i]==GREEN){
                guessWithColor+=ANSI_GREEN + guess.charAt(i);
            }
            else if(color[i]==YELLOW){
                guessWithColor+=ANSI_YELLOW + guess.charAt(i);

            }
            else if(color[i]==BLACK){
                guessWithColor+=ANSI_RESET + guess.charAt(i);
            }

        }
        guessWithColor+=ANSI_RESET;
        Guesses.add(guessWithColor);
    }


    static String createKey(){
        int aInt = (int)(Math.random() * validWords.length);
        String AWord=validWords[aInt];
        return AWord;
    }

}
