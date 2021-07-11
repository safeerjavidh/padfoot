import java.util.*;

class FlightTicket {

  static boolean aisleTicketBooked = false;
  static boolean skipAisleTicket = false;
  static boolean windowTicketBooked = false;
  static boolean skipWindowTicket = false;
  static boolean middleTicketBooked = false;

  // Setting entire seatContent array values as -1
  static int[][] setDefaultValue(int a[][],int total){
    for(int i=0;i<a.length;i++){
      for(int j=0;j<total;j++){
        a[i][j]=-1;
      }
    }
    return a;
  }

  //setting the seats value in the seatContent array as 0, left ll be -1 as we set previously
  static int[][] initializeSeats(int seatContent[][], int seats[][], int partition){
    int cursorPosition =0,j=0,k=0;
    for(int temp=0;temp<partition;temp++){
      int partitionRow = seats[temp][1];
      for(int i=0;i<partitionRow;i++){
        int seatLimitPerPartition = seats[temp][0];
        for(k=0,j=cursorPosition;k<seatLimitPerPartition;j++,k++){
          seatContent[i][j]=0;
        }
      }
      cursorPosition+=k+1;
    }
    return seatContent;
  }

  static int[][] getAisleSeat(int seatContent[][], int seats[][], int partition, int maxRow, int ticketNumber ){
    aisleTicketBooked=false;
    if(partition == 1 ) {
      return seatContent;
    }
    for(int i=0;i<maxRow;i++){
      int oldCursor=0, cursorPosition=-1;
      for(int j=0;j<partition;j++,cursorPosition++){
        int columnCount = seats[j][0];
        cursorPosition+=columnCount;
        if(columnCount == 1 && (j==0 || j==partition-1)){
          continue;
        }
        if(j==0){
          if(seatContent[i][cursorPosition] == 0){
            seatContent[i][cursorPosition] = ticketNumber;
            aisleTicketBooked = true;
            return seatContent;
          }
          oldCursor = cursorPosition;
        } else if(j== partition-1){
          if(seatContent[i][oldCursor+2] == 0) {
            seatContent[i][oldCursor+2] = ticketNumber;
            aisleTicketBooked = true;
            return seatContent;
          }
        } else if(seatContent[i][cursorPosition-columnCount+1] == 0) {
          seatContent[i][cursorPosition-columnCount+1] = ticketNumber;
          aisleTicketBooked = true;
          return seatContent;
        } else if(seatContent[i][cursorPosition]==0) {
          seatContent[i][cursorPosition] = ticketNumber;
          aisleTicketBooked = true;
          return seatContent;
        } else {
          oldCursor= cursorPosition;
        }
      }
    }
    return seatContent;
  }

  static int[][] getWindowSeat(int seatContent[][],int maxRow, int ticketNumber,int totalColumn) {
    windowTicketBooked= false;
    for(int i=0;i<maxRow;i++) {
        if(seatContent[i][0] == 0){
          seatContent[i][0]= ticketNumber;
          windowTicketBooked= true;
          return seatContent;
        } else if(seatContent[i][totalColumn-1] == 0){
          seatContent[i][totalColumn-1] = ticketNumber;
          windowTicketBooked= true;
          return seatContent;
        }
      }
    return seatContent;
  }
  static int[][] getMiddleSeat(int seatContent[][],int seats[][], int partition, int maxRow, int ticketNumber){
    middleTicketBooked= false;
    for(int i=0;i<maxRow;i++){
      int oldCursor=0, cursorPosition=-1;
      for(int j=0;j<partition;j++,cursorPosition++){
        oldCursor++;
        int columnCount = seats[j][0];
        cursorPosition+=columnCount;
        for(;oldCursor<=cursorPosition-1; oldCursor++){
          if( seatContent[i][oldCursor] == 0){
            seatContent[i][oldCursor] = ticketNumber;
            middleTicketBooked = true;
            return seatContent;
          }
        }
        oldCursor+=2;
      }
    }
    return seatContent;
  }
  static void print(int a[][], int total){
    for(int i=0;i<a.length;i++){
      for(int j=0;j<total;j++){
          int value = a[i][j];
          if(value>-1){
            // this is added to print output in a more readable way
            System.out.print( value<10 ? ("  "+value) : (" "+value));
          }else{
            System.out.print("   ");
          }

      }
      System.out.println("");
    }
  }
  public static void main(String args[]) {

    Scanner scan = new Scanner(System.in);
    System.out.println("Enter number of seats partition");
    int partition = scan.nextInt();
    int seats [][] = new int[partition][2];
    int length = seats.length;
    int totalColumn=0, maxRow=0;
    for(int i=0;i<length;i++) {
      System.out.println("Enter number of columns and rows in partition:"+(i+1));
      for(int j=0;j<2;j++) {
        int value = scan.nextInt();
        seats[i][j] = value;
        if(j == 0){
          totalColumn+=value;
        }
        if(j==1){
          if(maxRow<value){
            maxRow=value;
          }
        }
      }
    }
    totalColumn+= length-1;

    int seatContent[][] = new int[maxRow][totalColumn];
    seatContent = setDefaultValue(seatContent, totalColumn);

    print(seats, 2);
    seatContent = initializeSeats(seatContent, seats, partition);
    System.out.println("Zero represents available seats");
    print(seatContent, totalColumn);

    System.out.println("Enter nmber of tickets to book");
    int ticketsToBook = scan.nextInt();
    for(int i=1;i<=ticketsToBook;i++){
      if(!skipAisleTicket){
        seatContent = getAisleSeat(seatContent, seats, partition, maxRow, i );
      }
      if(!aisleTicketBooked && !skipWindowTicket ){
        seatContent = getWindowSeat(seatContent, maxRow, i, totalColumn);
        skipAisleTicket= true;
      }
      if(!aisleTicketBooked && !windowTicketBooked){
        seatContent = getMiddleSeat(seatContent, seats, partition, maxRow, i );
        skipWindowTicket= true;
      }
      if(!aisleTicketBooked && !windowTicketBooked && !middleTicketBooked){
        print(seatContent, totalColumn);
        System.out.println("Required number of tickets exceeded total number of available seats !!! Total seats allotted: "+ (i-1));
        return;
      }
    }
    print(seatContent, totalColumn);
  }
}
