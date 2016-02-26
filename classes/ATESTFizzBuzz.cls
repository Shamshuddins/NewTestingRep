public class ATESTFizzBuzz {    
     private final integer floor;
     private final integer ceiling;    

     public ATESTFizzBuzz(){
          floor      = 1;
          ceiling = 100;
     }
    
     public ATESTFizzBuzz(integer input_floor, integer input_ceiling){
          floor = input_floor;
          ceiling = input_ceiling;
     }
    
     public void run(){    
          for(String element : this.getFizzBuzzList()){
               system.debug(element);
          }
     }
    
     public List<string> getFizzBuzzList(){
          List<string> fizzBuzzList = new List<string>();
          for(integer i=floor; i <= ceiling; i++){
               string listElement = '';
               if( isFizz(i) ){
                    listElement = 'Fizz';
               }
               if( isBuzz(i) ){
                    listElement += 'Buzz';
               }
               if(listElement == ''){
                    listElement = string.valueOf(i);
               }
               fizzBuzzList.add(listElement);
              
          }

          return fizzBuzzList;
     }

    
     static final integer FIZZ_MULTIPLE = 3;
     private boolean isFizz(integer n){
          return ( math.mod(n, FIZZ_MULTIPLE) == 0);
     }
     
     static final integer BUZZ_MULTIPLE = 5;
     private boolean isBuzz(integer n){
          return ( math.mod(n, BUZZ_MULTIPLE) == 0);
     }

    
   /*  public static testmethod void fizzTests(){
          FizzBuzz fb = new FizzBuzz();
          System.assertEquals(false, fb.isFizz(1));         
          System.assertEquals(true,  fb.isFizz(3));
          System.assertEquals(false, fb.isFizz(5));
     }
    
     public static testmethod void buzzTests(){
          FizzBuzz fb = new FizzBuzz();
          System.assertEquals(false, fb.isBuzz(1));         
          System.assertEquals(false, fb.isBuzz(3));         
          System.assertEquals(true,  fb.isBuzz(5));
     }
    
     public static testmethod void fizzBuzzTests(){
          FizzBuzz fb = new FizzBuzz();
          System.assertEquals(true, fb.isFizz(15));
          System.assertEquals(true, fb.isBuzz(15));
     }
   
     public static testmethod void fizzBuzzListTests(){
          //Use a 0 based index range to make fetching/testing list offsets easier.
          FizzBuzz fb = new FizzBuzz(0, 100);
          System.assertEquals(101, fb.getFizzBuzzList().size() );
          System.assertEquals('1', fb.getFizzBuzzList().get(1) );
          System.assertEquals('2', fb.getFizzBuzzList().get(2) );
          System.assertEquals('Fizz', fb.getFizzBuzzList().get(3) );
          System.assertEquals('4', fb.getFizzBuzzList().get(4) );
          System.assertEquals('Buzz', fb.getFizzBuzzList().get(5) );
          System.assertEquals('FizzBuzz', fb.getFizzBuzzList().get(15) );
          System.assertEquals('FizzBuzz', fb.getFizzBuzzList().get(30) );
     }*/
}