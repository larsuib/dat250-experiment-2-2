package no.hvl.dat250.jpa.basicexample;

import no.hvl.dat250.jpa.basicexample.relationship.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final String PERSISTENCE_UNIT_NAME = "banking";
    private static EntityManagerFactory factory;

    public static void main(String[] args) {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
        // read the existing entries and write to console
        Query q = em.createQuery("select p from Person p where p.name = 'Max Mustermann'");
        List<Person> todoList = q.getResultList();
        for (Person person : todoList) {
            System.out.println(person);
        }
        System.out.println("Size: " + todoList.size());

        // create new todo
        em.getTransaction().begin();

        Person person = new Person();
        person.setName("Max Mustermann");

        // adding address
        Address address = new Address();
        address.setNumber(28);
        address.setStreet("Inndalsveien");
        ArrayList<Address> addresses = new ArrayList<>();
        addresses.add(address);
        person.setAddresses(addresses);

        // adding cards
        Pincode pincode = new Pincode();
        pincode.setCount(1);
        pincode.setPincode("123");

        CreditCard creditCard1 = new CreditCard();
        creditCard1.setNumber(12345);
        creditCard1.setBalance(-5000);
        creditCard1.setLimit(-10000);
        creditCard1.setPincode(pincode);

        CreditCard creditCard2 = new CreditCard();
        creditCard2.setNumber(123);
        creditCard2.setBalance(1);
        creditCard2.setLimit(2000);
        creditCard2.setPincode(pincode);

        Bank bank = new Bank();
        bank.setName("Pengebank");
        creditCard1.setBank(bank);
        creditCard2.setBank(bank);

        ArrayList<CreditCard> creditCards = new ArrayList<>();
        creditCards.add(creditCard1);
        creditCards.add(creditCard2);
        bank.setCreditCards(creditCards);
        person.setCreditCards(creditCards);

        em.persist(person);
        em.getTransaction().commit();

        // tests
        Query query = em.createQuery("select p from Person p where p.name = 'Max Mustermann'");
        List<Person> persons = q.getResultList();
        Person testPerson = persons.get(persons.size() - 1);

        System.out.println("\n==> Checking for correct values in `Person` (from task) <==");
        System.out.println("> Name:" + testPerson.getName().equals("Max Mustermann"));
        System.out.println("> Address Street:" + testPerson.getAddresses().get(0).getStreet().equals("Inndalsveien"));
        System.out.println("> Address Number:" + (testPerson.getAddresses().get(0).getNumber() == 28));
        System.out.println("> Credit Card 1 Number:" + (testPerson.getCreditCards().get(0).getNumber() == 12345));
        System.out.println("> Credit Card 1 Balance:" + (testPerson.getCreditCards().get(0).getBalance() == -5000));
        System.out.println("> Credit Card 1 Limit:" + (testPerson.getCreditCards().get(0).getLimit() == -10000));
        System.out.println("> Credit Card 1 Pincode:" +
                (testPerson.getCreditCards().get(0).getPincode().getPincode().equals("123") && testPerson.getCreditCards().get(0).getPincode().getCount() == 1));

        System.out.println("> Credit Card 2 Number:" + (testPerson.getCreditCards().get(1).getNumber() == 123));
        System.out.println("> Credit Card 2 Balance:" + (testPerson.getCreditCards().get(1).getBalance() == 1));
        System.out.println("> Credit Card 2 Limit:" + (testPerson.getCreditCards().get(1).getLimit() == 2000));
        System.out.println("> Credit Card 2 Pincode:" +
                (testPerson.getCreditCards().get(1).getPincode().getPincode().equals("123") && testPerson.getCreditCards().get(1).getPincode().getCount() == 1));
        System.out.println("> Bank:" + testPerson.getCreditCards().get(0).getBank().getName().equals("Pengebank"));

        em.close();
    }
}
