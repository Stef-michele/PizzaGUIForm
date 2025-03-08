import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PizzaGUIFrame extends JFrame {
    private JRadioButton thinCrust, regularCrust, deepDish;
    private JComboBox<String> sizeBox;
    private JCheckBox[] toppings;
    private JTextArea orderSummary;
    private JButton orderButton, clearButton, quitButton;


    private final double[] sizePrices = {8.00, 12.00, 16.00, 20.00};
    private final double toppingPrice = 1.00;
    private final double taxRate = 0.07;

    public PizzaGUIFrame() {
        setTitle("Stefania's Pizza Order Form");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createCrustPanel(), BorderLayout.NORTH);
        add(createSizePanel(), BorderLayout.WEST);
        add(createToppingsPanel(), BorderLayout.CENTER);
        add(createOrderPanel(), BorderLayout.EAST);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createCrustPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Crust Type"));
        ButtonGroup group = new ButtonGroup();
        thinCrust = new JRadioButton("Thin");
        regularCrust = new JRadioButton("Regular");
        deepDish = new JRadioButton("Deep-Dish");
        group.add(thinCrust);
        group.add(regularCrust);
        group.add(deepDish);
        panel.add(thinCrust);
        panel.add(regularCrust);
        panel.add(deepDish);
        return panel;
    }

    private JPanel createSizePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Size"));
        String[] sizes = {"Small", "Medium", "Large", "Super"};
        sizeBox = new JComboBox<>(sizes);
        panel.add(sizeBox);
        return panel;
    }

    private JPanel createToppingsPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Toppings"));
        String[] toppingNames = {"Pepperoni", "Mushrooms", "Peppers", "Onions", "Bacon", "Sausage", "Pineapple", "Anchovies", "Olives"};
        toppings = new JCheckBox[toppingNames.length];
        for (int i = 0; i < toppings.length; i++) {
            toppings[i] = new JCheckBox(toppingNames[i]);
            panel.add(toppings[i]);
        }
        return panel;
    }

    private JPanel createOrderPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Order Summary"));
        orderSummary = new JTextArea(15, 20);
        orderSummary.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderSummary);
        panel.add(scrollPane);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        orderButton = new JButton("Order");
        clearButton = new JButton("Clear");
        quitButton = new JButton("Quit");

        orderButton.addActionListener(new OrderListener());
        clearButton.addActionListener(e -> clearForm());
        quitButton.addActionListener(e -> confirmQuit());

        panel.add(orderButton);
        panel.add(clearButton);
        panel.add(quitButton);
        return panel;
    }

    private class OrderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String crust = thinCrust.isSelected() ? "Thin" : regularCrust.isSelected() ? "Regular" : deepDish.isSelected() ? "Deep-Dish" : "";
            if (crust.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select a crust type.");
                return;
            }

            int sizeIndex = sizeBox.getSelectedIndex();
            double basePrice = sizePrices[sizeIndex];
            String size = (String) sizeBox.getSelectedItem();

            StringBuilder orderText = new StringBuilder();
            orderText.append("====================================\n");
            orderText.append(String.format("%-20s %s\n", crust + " Crust, " + size, String.format("$%.2f", basePrice)));

            double toppingTotal = 0;
            for (JCheckBox topping : toppings) {
                if (topping.isSelected()) {
                    orderText.append(String.format("%-20s %s\n", topping.getText(), String.format("$%.2f", toppingPrice)));
                    toppingTotal += toppingPrice;
                }
            }
            if (toppingTotal == 0) {
                JOptionPane.showMessageDialog(null, "Please select at least one topping.");
                return;
            }

            double subTotal = basePrice + toppingTotal;
            double tax = subTotal * taxRate;
            double total = subTotal + tax;

            orderText.append("------------------------------------\n");
            orderText.append(String.format("%-20s %s\n", "Sub-total:", String.format("$%.2f", subTotal)));
            orderText.append(String.format("%-20s %s\n", "Tax:", String.format("$%.2f", tax)));
            orderText.append("====================================\n");
            orderText.append(String.format("%-20s %s\n", "Total:", String.format("$%.2f", total)));

            orderSummary.setText(orderText.toString());
        }
    }

    private void clearForm() {
        thinCrust.setSelected(false);
        regularCrust.setSelected(false);
        deepDish.setSelected(false);
        sizeBox.setSelectedIndex(0);
        for (JCheckBox topping : toppings) {
            topping.setSelected(false);
        }
        orderSummary.setText("");
    }

    private void confirmQuit() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

}
