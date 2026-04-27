import java.io.*;
import java.util.*;

/* ResourceManagement
 *
 * Stores the information needed to decide which items to purchase for the given budget and departments
 */
public class ResourceManagement
{
    private PriorityQueue<Department> departmentPQ; /* priority queue of departments */
    private Double remainingBudget;                 /* the budget left after purchases are made (should be 0 after the constructor runs) */
    private Double budget;                          /* the total budget allocated */
    private List<Department> allDepartments = new ArrayList<>();

    /* TO BE COMPLETED BY YOU
    * Fill in your name in the function below
    */
    public static void printName( )
      {
        /* TODO : Fill in your name */
        System.out.println("This solution was completed by:");
        System.out.println("Brandon Ikwuagwu");
        System.out.println("Eddy Kwes");
      }

      /* Constructor for a ResourceManagement object
       * TODO
       * Simulates the algorithm from the pdf to determine what items are purchased
       * for the given budget and department item lists.
       */

        public ResourceManagement( String fileNames[], Double budget )
        {
            this.budget = budget;
            this.remainingBudget = budget;
            this.departmentPQ = new PriorityQueue<>();

            // PHASE 1: Load ALL departments first
            for (String fileName : fileNames) {
                File file = new File(fileName);
                Scanner input;
                try {
                    input = new Scanner(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    System.out.println(fileName + " not found.");
                    continue;
                }

                String name = input.next();
                Department dept = new Department(name);

                while (input.hasNext()) {
                    String itemName = input.next();
                    Double itemPrice = input.nextDouble();
                    dept.itemsDesired.add(new Item(itemName, itemPrice));
                }

                departmentPQ.add(dept);
                allDepartments.add(dept);
                input.close();

            }

            // PHASE 2: Run purchasing algorithm AFTER all departments are loaded
            while (remainingBudget > 0 && !departmentPQ.isEmpty()) {
                Department dept = departmentPQ.poll();

                while (!dept.itemsDesired.isEmpty() && dept.itemsDesired.peek().price > remainingBudget) {
                    dept.itemsRemoved.add(dept.itemsDesired.poll());
                }

                String purchasedName;
                double amountSpent;

                if (dept.itemsDesired.isEmpty()) {
                    amountSpent = Math.min(1000.0, remainingBudget);
                    purchasedName = "Scholarship";
                } else {
                    Item item = dept.itemsDesired.poll();
                    amountSpent = item.price;
                    purchasedName = item.name;
                    dept.itemsReceived.add(item);
                }

                dept.priority += amountSpent;
                remainingBudget -= amountSpent;

                String price = String.format("$%.2f", amountSpent);
                System.out.printf("Department of %-30s- %-30s- %30s\n",
                        dept.name, purchasedName, price);

                if (!dept.itemsDesired.isEmpty() || !dept.itemsRemoved.isEmpty()) {
                    departmentPQ.add(dept);
                }
            }
        }


      /* printSummary
       * TODO
       * Print a summary of what each department received and did not receive.
       * Be sure to also print remaining items in each itemsDesired Queue.
       */
      public void printSummary( ){

        for(Department dept : allDepartments) {

            System.out.println();
            System.out.println("Current Department is: " + dept.name);

            System.out.println("Total Spent: " + dept.priority);

            System.out.println("  Items Received:");
            for (Item item : dept.itemsReceived) {
                String price = String.format("$%.2f", item.price);
                System.out.printf("  %-30s - %30s\n", item.name, price);
            }
        }
      }
    }

    /* Department
     *
     * Stores the information associated with a Department at the university
     */
    class Department implements Comparable<Department>
    {
      String name;                /* name of this department */
      Double priority;            /* total money spent on this department */
      Queue<Item> itemsDesired;   /* list of items this department wants */
      Queue<Item> itemsReceived;  /* list of items this department received */
      Queue<Item> itemsRemoved;   /* list of items that were skipped because they exceeded the remaining budget */

      /* TODO
       * Constructor to build a Department from the information in the given fileName
       */
      public Department( String name ){
        /* Open the fileName, create items based on the contents, and add those items to itemsDesired */
          this.name = name;
          this.priority = 0.0;
          this.itemsDesired = new LinkedList<>();
          this.itemsReceived = new LinkedList<>();
          this.itemsRemoved = new LinkedList<>();
      }

      /*
       * Compares the data in the given Department to the data in this Department
       * Returns -1 if this Department comes first
       * Returns 0 if these Departments have equal priority
       * Returns 1 if the given Department comes first
       *
       * This function is to ensure the departments are sorted by the priority when put in the priority queue
       */
      public int compareTo( Department dept ){
        return this.priority.compareTo( dept.priority );
      }

      public boolean equals( Department dept ){
        return this.name.compareTo( dept.name ) == 0;
      }

      @Override
      @SuppressWarnings("unchecked") //Suppresses warning for cast
      public boolean equals(Object aThat) {
        if (this == aThat) //Shortcut the future comparisons if the locations in memory are the same
          return true;
        if (!(aThat instanceof Department))
          return false;
        Department that = (Department)aThat;
        return this.equals( that ); //Use above equals method
      }

      @Override
      public int hashCode() {
        return name.hashCode(); /* use the hashCode for data stored in this name */
      }

      /* Debugging tool
       * Converts this Department to a string
       */
      @Override
      public String toString() {
        return "NAME: " + name + "\nPRIORITY: " + priority + "\nDESIRED: " + itemsDesired + "\nRECEIVED " + itemsReceived + "\nREMOVED " + itemsRemoved + "\n";
      }
    }

    /* Item
     *
     * Stores the information associated with an Item which is desired by a Department
     */
    class Item
    {
      String name;    /* name of this item */
      Double price;   /* price of this item */

      /*
       * Constructor to build a Item
       */
      public Item( String name, Double price ){
        this.name = name;
        this.price = price;
      }

      /* Debugging tool
       * Converts this Item to a string
       */
      @Override
      public String toString() {
        return "{ " + name + ", " + price + " }";
      }
    }
