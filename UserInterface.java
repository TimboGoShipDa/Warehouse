import java.util.*;
import java.text.*;
import java.io.*;

public class UserInterface {
    private static UserInterface userInterface;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private static final int EXIT             = 0;
    private static final int ADD_CLIENT       = 1;
    private static final int ADD_PRODUCT      = 2;
    private static final int ADD_SUPPLIER     = 3;
    private static final int ASSIGN_PRODUCT   = 4;
    private static final int SHOW_CLIENTS     = 5;
    private static final int SHOW_PRODUCTS    = 6;
    private static final int SHOW_SUPPLIERS   = 7;
    private static final int SAVE             = 8;
    private static final int MENU             = 9;

    private UserInterface() {
        if (yesOrNo("Look for saved data and use it?")) {
            retrieve();
        }
        else {
            warehouse = Warehouse.instance();
        }
    }

    public static UserInterface instance() {
        if (userInterface == null) {
            return userInterface = new UserInterface();
        }
        else {
            return userInterface;
        }
    }

    public String getToken(String prompt) {
        do {
            try {
                System.out.print(prompt);
                String line = reader.readLine();
                StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
                if (tokenizer.hasMoreTokens()) {
                    return tokenizer.nextToken();
                }
            }
            catch (IOException ioe) {
                System.exit(0);
            }
        } while (true);
    }

    private boolean yesOrNo(String prompt) {
        String more = getToken(prompt + " (Y|y)[es] or anything else for no: ");
        if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
            return false;
        }
        return true;
    }

    public int getNumber(String prompt) {
        do {
            try {
                String item = getToken(prompt);
                Integer num = Integer.valueOf(item);
                return num.intValue();
            }
            catch (NumberFormatException nfe) {
                System.out.println("Please input a number ");
            }
        } while (true);
    }

    public Calendar getDate(String prompt) {
        do {
            try {
                Calendar date = new GregorianCalendar();
                String item = getToken(prompt);
                DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
                date.setTime(df.parse(item));
                return date;
            }
            catch (Exception fe) {
                System.out.println("Please input a date as mm/dd/yy");
            }
        } while (true);
    }

    public int getCommand() {
        do {
            try {
                int value = Integer.parseInt(getToken("> "));
                if (value >= EXIT && value <= MENU) {
                    return value;
                }
            }
            catch (NumberFormatException nfe) {
                System.out.println("Enter a number");
            }
        } while (true);
    }

    public void menu() {
        System.out.println("                Warehouse System\n"
                         + "                     Stage 1\n\n"
                         + "       +-------------------------------+\n"
                         + "       | 1) Add Client                 |\n"
                         + "       | 2) Add Product                |\n"
                         + "       | 3) Add Supplier               |\n"
                         + "       | 4) Assign Product to Supplier |\n"
                         + "       | 5) Show Clients               |\n"
                         + "       | 6) Show Products              |\n"
                         + "       | 7) Show Suppliers             |\n"
                         + "       | 8) Save State                 |\n"
                         + "       | 9) Display Menu               |\n"
                         + "       | 0) Exit                       |\n"
                         + "       +-------------------------------+\n");
    }

    public void addClient() {
        String name = getToken("Enter client company: ");
        String address = getToken("Enter address: ");
        String phone = getToken("Enter phone: ");
        Client result;
        result = warehouse.addClient(name, address, phone);
        if (result == null) {
            System.out.println("Client could not be added.");
        }
        System.out.println(result);
    }

    public void addProduct() {
        Product result;
        String prodName = getToken("Enter product name: ");
        String quantity = getToken("Enter quantity: ");
        String price = getToken("Enter price per unit: $");
        result = warehouse.addProduct(prodName, quantity, price);
        if (result != null) {
            System.out.println(result);
        }
        else {
            System.out.println("Product could not be added.");
        }
    }

    public void addSupplier() {
        System.out.println("NEED TO IMPLEMENT!");
    }

    public void assignProduct() {
        System.out.println("NEED TO IMPLEMENT!");
    }

    public void showClients() {
        Iterator allClients = warehouse.getClients();
        while (allClients.hasNext()) {
            Client client = (Client)(allClients.next());
            System.out.println(client.toString());
        }
    }

    public void showProducts() {
        Iterator allProducts = warehouse.getProducts();
        while (allProducts.hasNext()){
            Product product = (Product)(allProducts.next());
            System.out.println(product.toString());
        }
    }

    public void showSupplies() {
        System.out.println("NEED TO IMPLEMENT!");
    }

    private void save() {
        if (warehouse.save()) {
            System.out.println(" The warehouse has been successfully saved in the file WarehouseData \n" );
        }
        else {
            System.out.println(" There has been an error in saving \n" );
        }
    }

    private void retrieve() {
        try {
            Warehouse tempWarehouse = Warehouse.retrieve();
            if (tempWarehouse != null) {
                System.out.println(" The warehouse has been successfully saved in the file WarehouseData \n" );
                warehouse = tempWarehouse;
            } else {
                System.out.println("File doesnt exist; creating new warehouse" );
                warehouse = Warehouse.instance();
            }
        } catch(Exception cnfe) {
            cnfe.printStackTrace();
        }
    }

    public void process() {
        int command;
        menu();
        while ((command = getCommand()) != EXIT) {
            switch (command) {
                case ADD_CLIENT:        addClient();
                break;
                case ADD_PRODUCT:       addProduct();
                break;
                case ADD_SUPPLIER:      addSupplier();
                break;
                case ASSIGN_PRODUCT:    assignProduct();
                break;
                case SHOW_CLIENTS:      showClients();
                break;
                case SHOW_PRODUCTS:     showProducts();
                break;
                case SHOW_SUPPLIERS:    showSupplies();
                break;
                case SAVE:              save();
                break;
                case MENU:              menu();
                break;
            }
        }
    }

    public static void main(String[] s) {
        try {
            Client c1 = null;
            System.out.println(c1.toString());
        }
        catch (NullPointerException ne) {
            System.out.println("No problem; keep going");
        }

        UserInterface.instance().process();
    }
}
