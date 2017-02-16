package pageminroutegui;
//Brian Dorsey 2016
//Custom hashmap for key frequency.

public class bHashMap implements java.io.Serializable{
    private static final long serialVersionUID = 5280;
    
    private int tableSize; //the size of the table.
    private bucket[] table; //list of buckets

    public bHashMap(int size) {
        tableSize = size;
        table = new bucket[tableSize];
    }

    public bucket[] getList(){
        return this.table;
    }
    
    public void debugIt(){
        for(int x = 0; x < table.length; x++){
            if(table[x] != null){
                System.out.println(table[x].key);
            }
        }
    }
    
    public void put(String k) { //adds the string item to the map, includes collision control.
        //System.out.println(k);
        int hash = genHash(k);
        if (table[hash] != null) {
            if (!table[hash].key.equals(k)) {
                bucket t = table[hash];
                while (t.next != null) {
                    t = t.next;
                    if (t.key.equals(k)) {
                        t.increment();
                    }
                }

                bucket q = new bucket(k);
                t.next = q;
            } else {
                table[hash].increment();
            }

        } else {
            table[hash] = new bucket(k);
        }

    }

    public int get(String k) { // gets the item from the map via the key. Searches using the hash, then checks each bucket for the word with same hash.
        int hash = genHash(k);
        if (table[hash] == null) {
            return -1;
        } else if (table[hash].key.equals(k)) {
            return table[hash].val;
        } else {
            bucket t;
            for (t = table[hash]; t.next != null; t = t.next) {
                if (t.key.equals(k)) {
                    break;
                }
            }
            return t.val;
        }
    }

//Function removed, unused. No support for collided keys.
//    public void remove(String k) {
//        int hash = genHash(k);
//        if (table[hash] != null) {
//            table[hash] = null;
//        }
//    }
    public boolean containsKey(String k) {//returns boolean true if the key is in the map, false if it is not.
        //ok we searched for the hash but didnt check if the strings matched.
        int hash = genHash(k);
        if (table[hash] == null) {
            return false;
        } else if (table[hash].key.equals(k)) {
            return true;
        } else {
            bucket t;
            for (t = table[hash]; t.next != null; t = t.next) {
                if (t.key.equals(k)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int genHash(String word) { // Function to generate hash code for a string.
        int i;
        int sum;

        for (sum = 0, i = 0; i < word.length(); i++) {
            sum += word.charAt(i);
        }
        return sum % this.tableSize;
    }

    public static class bucket implements java.io.Serializable{ // Bucket class for hashmap

        String key;
        int val;
        bucket next;//next bucket in the linked list.

        private bucket(String k) {//Constructor for hashmap bucket.
            key = k;
            val = 1;//Every key stars with frequency 1.
        }

        private void increment() {//Increases this key's frequency count.
            this.val++;
        }
    }

}
