/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazyjpatest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import lazyjpatest.entity.Customer;
import lazyjpatest.entity.DiscountCode;
import lazyjpatest.entity.PurchaseOrder;

/**
 *
 * @author kuw
 */
public class LazyJPATest {

    private static final String CONFIG_FILE = "/META-INF/config.properties";
    private static final String PROP_PU = "persistence_unit_name";

    private final EntityManager em;
    private List<Customer> customers;
    Properties config;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LazyJPATest test = new LazyJPATest();
        if (test.em != null) {
            test.getCustomers();
            test.printCustomers();
        } else {
            Logger.getLogger(LazyJPATest.class.getName()).log(Level.WARNING, "Cannot run test without a configured entity manager!");
        }
        System.exit(0);
    }

    public LazyJPATest() {
        try {
            InputStream configStream = LazyJPATest.class.getResourceAsStream(CONFIG_FILE);
            this.config = new Properties();
            this.config.load(configStream);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LazyJPATest.class.getName()).log(Level.SEVERE, "{0} file not found in {1}!", CONFIG_FILE);
            this.em = null;
            return;
        } catch (IOException ex) {
            Logger.getLogger(LazyJPATest.class.getName()).log(Level.SEVERE, "Error reading properties from {0} file: {1}", new Object[]{CONFIG_FILE, ex.getMessage()});
            this.em = null;
            return;
        }

        String persistenceUnit = this.config.getProperty(PROP_PU);
        this.em = Persistence.createEntityManagerFactory(persistenceUnit).createEntityManager();
    }

    public void getCustomers() {
        TypedQuery<Customer> customersQuery = em.createNamedQuery("Customer.findAll", Customer.class);
        try {
            customers = customersQuery.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(LazyJPATest.class.getName()).log(Level.SEVERE, "Could not retrieve customers: {0}", ex.getMessage());
        }
    }

    public void printCustomers() {
        if (this.customers != null) {
            System.out.println("CUSTOMERS");
            System.out.println("==========================================");

            for (Customer customer : this.customers) {
                DiscountCode dc = customer.getDiscountCode();
                List<PurchaseOrder> purchaseOrders = customer.getPurchaseOrderList();
                System.out.print("ID: " + customer.getCustomerId());
                System.out.print(", Name: " + customer.getName());
                System.out.print(", Email: " + customer.getEmail());
                System.out.print(", Discount: " + dc.getRate() + "%");
                System.out.println("");
                if (purchaseOrders != null && purchaseOrders.size() > 0) {
                    for (PurchaseOrder po : purchaseOrders) {
                        System.out.print("   --> Order#: " + po.getOrderNum());
                        System.out.print(", Product: " + po.getProductId().getDescription());
                        System.out.print(" (" + po.getProductId().getProductCode().getDescription() + ")");
                        System.out.print(", Qty: " + po.getQuantity());
                        System.out.println("");
                    }
                } else {
                    System.out.println("    --> No purchase orders found!");
                }
            }
        }
    }
}
