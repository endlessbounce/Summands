package summands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Summands {

    public class Summand implements SummandsInterface{
        private ArrayList<String> list;
        private Map<String, long[]> all;
        
        //finds possible combinations of summands and adds to the ArrayList
        private void pickOutStrings(long n, long k, String current){
            long dif = n - k;
            
            if(dif <= 0){
                return;
            }

            list.add(current + k + "_" + dif);
            current += k + "_";
            
            for (long i = k; i <= (dif/2 - 1); i++) {

                pickOutStrings(dif, (i+1), current);
                
            }
                     
        }
        
        //compares arrays to check if they contain the same summands
        private boolean compareArrays(long arr[]){
            Set<String> keys = all.keySet();
            Iterator<String> iterator = keys.iterator();
            int i = 0;
            while(iterator.hasNext()){
                
                String key = iterator.next();
                long [] arr2 = all.get(key);
                
                if(arr2.length == arr.length){
                    
                    for (int j = 0; j < arr.length; j++) {
                        
                        //as arrays are sorted and of equal lengths
                        if(arr2[j] != arr[j]){
                            return true;
                        }

                    }
                    
                    return false;
                }
                
            }
            
            return true;
        }
        
        private void removeDuplicates(){
            //deletes strings containing not unique digits
            outer: for(int i = 0; i < list.size(); i++) {
                String stringNums []= list.get(i).split("_");
                
                for(int j = 0; j < stringNums.length; j++){    
                    String compared = stringNums[j];
                    
                    for(int l = j+1; l < stringNums.length; l++){
                        String comparedTo = stringNums[l];
                        
                        //remove strings having repeating summands 
                        if(compared.equals(comparedTo)){    
                            list.remove(i);
                            i--;
                            continue outer;
                        }
                        
                    }
                    
                }
                
            }
            
        }
        
        private long[] stringToLongArray(String strToParse){
            String stringNums []= strToParse.split("_");
            long[] longArr = new long[stringNums.length];
            
            for(int j = 0; j < stringNums.length; j++){    
                //parse number and add to the array
                longArr[j] = Long.parseLong(stringNums[j]);
            }

            Arrays.sort(longArr);
            
            return longArr;
        }
        
        @Override
        public long[] maxProduct(long n) {
            list = new ArrayList<>();
            long[] results = null;
            long[] resultsTemp = null;
            long multiplication = 1;
            long multiplicationTemp = 1;
            all = new HashMap<String, long[]>();
            
            if(n < 3){
                System.out.println("Please enter value greater than 2 to proceed calculations");
                return new long[0];
            }
            
            for (int i = 1; i <= n/2; i++) {
                pickOutStrings(n, i, "");
            }
            
            removeDuplicates();
            
            for(int i = 0; i < list.size(); i++) {
                resultsTemp = stringToLongArray(list.get(i));
                multiplicationTemp = 1;
                
                //count product of numbers in the string
                for(int j = 0; j < resultsTemp.length; j++){    
                    multiplicationTemp *= resultsTemp[j];
                }
                
                if(results == null){
                    results = resultsTemp;
                    multiplication = multiplicationTemp;
                }else{
                    
                    if(multiplicationTemp > multiplication){
                        results = resultsTemp;
                        multiplication = multiplicationTemp;
                        all.clear();
                        all.put(list.get(i), results);
                    }
                    
                    if(multiplicationTemp == multiplication){
                        results = resultsTemp;
                        boolean isNotACopy = compareArrays(results);
                        
                        //add only unique sets of summands
                        if(isNotACopy){
                            all.put(list.get(i), results);
                        }
                        
                    }
                    
                }
                
            }
            
            return results;
        }

        @Override
        public long[][] allMaxProduct(long n) {
            long allResults [][] = null;
            
            maxProduct(n);
            
            allResults = new long[all.size()][];
            
            Set<String> keys = all.keySet();
            Iterator<String> iterator = keys.iterator();
            int i = 0;
            while(iterator.hasNext()){
                
                String key = iterator.next();
                allResults[i] = all.get(key);
                i++;
            }
            
            return allResults;
        }

        @Override
        public long[] maxPairProduct(long n) {
            list = new ArrayList<>();
            long[] results = null;
            long[] resultsTemp = null;
            long summMax = 1;
            long summTemp = 1;
            
            if(n < 3){
                System.out.println("Please enter value greater than 2 to proceed calculations");
                return new long[0];
            }
            
            for (int i = 1; i <= n/2; i++) {
                pickOutStrings(n, i, "");
            }
            
            removeDuplicates();
            
            for(int i = 0; i < list.size(); i++) {
                resultsTemp = stringToLongArray(list.get(i));
                summTemp = 0;
                
                for (int j = 0; j < resultsTemp.length; j++) {
                    for (int k = j+1; k < resultsTemp.length; k++) {
                        summTemp += resultsTemp[j]*resultsTemp[k];
                    }
                }
                
                if(results == null || summTemp > summMax){
                    results = resultsTemp;
                    summMax = summTemp;
                }
            }
            
            return results;
        }
    
    }
    
    public static void main(String[] args) {
        Summands.Summand sumnd = new Summands().new Summand();
        
        //test maxProduct()
        long arr[] = sumnd.maxProduct(17);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ", ");
        }
        System.out.println();
        
        //test for allMaxProduct(). As per the task
        //it returns only one copy of array with unique set of summands
        long arrAll[][] = sumnd.allMaxProduct(25);
        for (int i = 0; i < arrAll.length; i++) {

            for (int j = 0; j < arrAll[i].length; j++) {
                System.out.print(arrAll[i][j] + ", ");
            }
            System.out.println();
        }
        
        //test maxPairProduct()
        long arrAllnew[] = sumnd.maxPairProduct(14);
        for (int i = 0; i < arrAllnew.length; i++) {
            System.out.print(arrAllnew[i] + ", ");
        }
        
    }
    
}
