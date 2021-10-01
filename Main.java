/**
 * Skeleton Program code for the AQA AS Paper 1 Summer 2016 examination
 * This code should be used in conjunction with the Preliminary Material
 * written by the AQA Programmer Team
 * Additional classes AQAConsole2016, AQAReadTextFile2016 and
 * AQAWriteTextFile2016 may be used.
 *
 * A package name may be chosen and private and public modifiers added.
 * Permission to make these changes to the Skeleton Program does not
 * need to be obtained from AQA/AQA Programmer
 *
 * Version Number 2.0
 *
 * In this version changes have been made to the comments prior to this line
 * and an unnecessary parameter (ships) removed from the CheckWin interface
 *
 */

import java.util.Random;

public class Main {
  AQAConsole2016 console = new AQAConsole2016();
  Random random = new Random();

  int[] getRowColumn(){
    int column = 0;
    int row = 0;
    int[] move;
    boolean notValid = true;
    move = new int[2];
    console.println();
    while (notValid) {
      column = console.readInteger("Please enter column: ");
      row = console.readInteger("Please enter row: ");
      console.println();
      if (row > 9 || row < 0 || column > 9 || column < 0) {
        console.println("Not Valid.");
      }else{
        notValid = false;
      }
    }
    move[0] = row;
    move[1] = column;
    return move;
  }

  void makePlayerMove(char[][] board, Ship[] ships){
    int[] rowColumn = getRowColumn();
    int row = rowColumn[0];
    int column = rowColumn[1];
    if (board[row][column] == 'm' || board[row][column] == 'h'){
      console.println("Sorry, you already shot at the square (" + column + "," + row + "). Please try again.");
    }
    else if (board[row][column] == '-'){
      console.println("Sorry, (" + column + "," + row + ") is a miss.");
      board[row][column] = 'm';
    }
    else{
      console.println("Hit at (" + column + "," + row + ").");
      board[row][column] = 'h';
    }
  }

  void setUpBoard(char[][] board){
    for(int row = 0; row<10; row++){
      for(int column = 0; column<10; column++){
        board[row][column] = '-';
      }
    }
  }

  void loadGame(String filename, char[][] board){
    AQAReadTextFile2016 boardFile = new AQAReadTextFile2016(filename);
    for(int row = 0; row<10; row++){
      String line = boardFile.readLine();
      for(int column = 0; column<10; column++){
        board[row][column] = line.charAt(column);
      }
    }
    boardFile.closeFile();
  }

  void placeRandomShips(char[][] board, Ship[] ships){
    char orientation = ' ';
    int row = 0;
    int column = 0;
    int HorV = 0;
    for(int i=0; i<ships.length; i++){
      boolean valid = false;
      while(!valid){
        row = random.nextInt(10);
        column = random.nextInt(10);
        HorV = random.nextInt(2);
        if (HorV == 0){
          orientation = 'v';
        }
        else{
          orientation = 'h';
        }
        valid = validateBoatPosition(board, ships[i], row, column, orientation);
      }
      console.println("Computer placing the " + ships[i].name);
      placeShip(board, ships[i], row, column, orientation);
    }
  }

  void placeShip(char[][] board, Ship ship, int row, int column, char orientation){
    if(orientation == 'v'){
      for (int scan = 0; scan < ship.size; scan++){
        board[row + scan][column] = ship.name.charAt(0);
      }
    }
    else if (orientation == 'h'){
      for (int scan = 0; scan < ship.size; scan++){
        board[row][column + scan] = ship.name.charAt(0);
      }
    }
  }

  boolean validateBoatPosition(char[][] board, Ship ship, int row, int column, char orientation){
    if(orientation == 'v' && row + ship.size > 10){
      return false;
    }
    else if(orientation == 'h' && column + ship.size > 10){
      return false;
    }
    else{
      if(orientation == 'v'){
        for (int scan = 0; scan < ship.size; scan++){
          if (board[row + scan][column] != '-'){
            return false;
          }
        }
      }
      else if(orientation == 'h'){
        for (int scan = 0; scan < ship.size; scan++){
          if(board[row][column + scan] != '-'){
            return false;
          }
        }
      }
    }
    return true;
  }

  boolean checkWin(char[][] board){
    for(int row = 0; row<10; row++){
      for(int column = 0; column<10; column++){
        if(board[row][column] == 'A' || board[row][column] == 'B' || board[row][column] == 'S' || board[row][column] == 'D' || board[row][column] == 'P'){
          return false;
        }
      }
    }
    return true;
  }

  void printBoard(char[][] board){
    console.println();
    console.println("The board looks like this: ");
    console.println();
    console.print (" ");
    for(int column = 0; column<10; column++){
      console.print(" " + column + "  ");
    }
    console.println();
    for(int row = 0; row<10; row++){
      console.print (row + " ");
      for(int column = 0; column<10; column++){
        if(board[row][column] == '-'){
          console.print(" ");
        }
        else if (board[row][column] == 'A' || board[row][column] == 'B' || board[row][column] == 'S' || board[row][column] == 'D' || board[row][column] == 'P'){
          console.print(" ");
        }
        else{
          console.print(board[row][column]);
        }
        if(column != 9){
          console.print(" | ");
        }
      }
      console.println();
    }
  }

  void displayMenu(){
    console.println("MAIN MENU");
    console.println();
    console.println("1. Start new game");
    console.println("2. Load training game");
    console.println("3. Load saved game");
    console.println("9. Quit");
    console.println();
  }

  int getMainMenuChoice(){
    int choice = console.readInteger("Please enter your choice: ");
    console.println();
    return choice;
  }

  void playGame(char[][] board, Ship[] ships){
    boolean gameWon = false;
    boolean gameLost = false;
    int torpedos = 20;
    while(!gameWon && !gameLost){
      printBoard(board);
      console.println("You have "+torpedos+" Torpedos left");
      makePlayerMove(board, ships);
      torpedos -= 1;
      gameWon = checkWin(board);
      gameLost = torpedos <= 0;
      if(gameWon){
        console.println("All ships sunk!");
        console.println();
      } else if (gameLost) {
        console.println("GAME OVER! You ran out of ammo");
        console.println();
      }
    }
  }

  class Ship{
    String name;
    int size;
  }

  void setUpShips(Ship[] ships){
    ships[0] = new Ship();
    ships[0].name = "Aircraft Carrier";
    ships[0].size = 5;
    ships[1] = new Ship();
    ships[1].name = "Battleship";
    ships[1].size = 4;
    ships[2] = new Ship();
    ships[2].name = "Submarine";
    ships[2].size = 3;
    ships[3] = new Ship();
    ships[3].name = "Destroyer";
    ships[3].size = 3;
    ships[4] = new Ship();
    ships[4].name = "Patrol Boat";
    ships[4].size = 2;
  }

  public Main() {
    final String TrainingGame = "Training.txt";
    char board[][] = new char[10][10];
    Ship ships[] = new Ship[5];
    int menuOption = 0;
    boolean gameQuit = false;
    while(!gameQuit){
      setUpBoard(board);
      setUpShips(ships);
      displayMenu();
      menuOption = getMainMenuChoice();
      if (menuOption == 1){
        placeRandomShips(board, ships);
        playGame(board, ships);
      }
      if (menuOption == 2){
        loadGame(TrainingGame, board);
        playGame(board, ships);
      }
      if (menuOption == 3){
        console.println("Option 3 Executed");
      }
      if (menuOption == 9) {
        char quitChoice = console.readChar("Do you want to quit (y/n) ");
        if () {
          
        }
      }
    }
  }

/**
 * @param args the command line arguments
*/
  public static void main(String[] args) {
    new Main();
  }

}