package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    private JPanel content;
    private JPanel menuPanel;
    private JPanel customerPanel;
    private JPanel companyPanel;
    private JPanel employeePanel;
    private JButton companyButton;
    private JButton employeeButton;
    private JButton customerButton;
    private JPanel companyMenu;
    private JPanel addTruckPanel;
    private JButton gotoNewTruckPanel;
    private JPanel customerMenu;
    private JButton shippingRequestButton;
    private JPanel shippingRequestPanel;
    private JButton makeShippingRequestButton;
    private JButton addTruckButton;
    private JButton backToMainMenuButtonCU;
    private JButton backToMainMenuButtonCO;
    private JPanel employeeMenu;
    private JPanel unexpectedCostPanel;
    private JButton unexpectedCostsButton;
    private JButton backToMainMenuButtonEM;
    private JTextField truckTypeTextField;
    private JButton backToCompanyMenuButton;
    private JComboBox<String> truckTypesComboBox;
    private JTextField manufacturingYearTextField;
    private JTextField registrationNumberTextField;
    private JButton goToRoutePanelButton;
    private JPanel addRoutePanel;
    private JButton addRouteButton;
    private JButton routePToCompanyP;
    private JTextField city1TextField;
    private JTextField city2TextField;
    private JTextField distanceTextField;
    private JButton shippingReqPToCustomerPButton;
    private JTextField yourBidTextField;
    private JTextField shippingRouteC1TextField;
    private JTextField shippingRouteC2TextField;
    private JTextField requireRefrigeratedTransportYesTextField;
    private JTextField shippingWeightTextField;
    private JButton goToScheduleSPButton;
    private JButton schsToCPButton;
    private JTextField scheduleShippingID;
    private JPanel scheduleShippingPanel;
    private JButton scheduleShippingButton;
    private JButton ueToEMButton;
    private JButton addUnexpectedCostButton;
    private JTextField unexpectedCostTextField2;
    private JTextField unexpectedCostTextField1;
    final String[] truckTypes = {"1.Normal", "2.Refrigerated", "3.Large"};

    public App() {
        companyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPanel.setVisible(false);
                companyPanel.setVisible(true);
            }
        });
        customerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPanel.setVisible(false);
                customerPanel.setVisible(true);
            }
        });
        employeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPanel.setVisible(false);
                employeePanel.setVisible(true);
            }
        });

        backToMainMenuButtonCU.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customerPanel.setVisible(false);
                menuPanel.setVisible(true);
            }
        });
        backToMainMenuButtonCO.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                companyPanel.setVisible(false);
                menuPanel.setVisible(true);
            }
        });
        backToMainMenuButtonEM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                employeePanel.setVisible(false);
                menuPanel.setVisible(true);
            }
        });
        unexpectedCostsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                employeeMenu.setVisible(false);
                unexpectedCostPanel.setVisible(true);
            }
        });

        shippingRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customerMenu.setVisible(false);
                shippingRequestPanel.setVisible(true);
            }
        });
        backToCompanyMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTruckPanel.setVisible(false);
                companyMenu.setVisible(true);
            }
        });
        gotoNewTruckPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                companyMenu.setVisible(false);
                addTruckPanel.setVisible(true);

            }
        });
        routePToCompanyP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRoutePanel.setVisible(false);
                companyMenu.setVisible(true);
            }
        });
        goToRoutePanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                companyMenu.setVisible(false);
                addRoutePanel.setVisible(true);
            }
        });
        shippingReqPToCustomerPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shippingRequestPanel.setVisible(false);
                customerMenu.setVisible(true);
            }
        });
        schsToCPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scheduleShippingPanel.setVisible(false);
                companyMenu.setVisible(true);
            }
        });
        goToScheduleSPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                companyMenu.setVisible(false);
                scheduleShippingPanel.setVisible(true);
            }
        });
        ueToEMButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unexpectedCostPanel.setVisible(false);
                employeeMenu.setVisible(true);
            }
        });
    }


    public JPanel getContent() {
        return content;
    }

    public JButton getAddUnexpectedCostButton() {
        return addUnexpectedCostButton;
    }

    public JTextField getUnexpectedCostTextField2() {
        return unexpectedCostTextField2;
    }

    public JTextField getUnexpectedCostTextField1() {
        return unexpectedCostTextField1;
    }

    public JButton getMakeShippingRequestButton() {
        return makeShippingRequestButton;
    }

    public JButton getAddTruckButton() {
        return addTruckButton;
    }

    public JComboBox<String> getTruckTypesComboBox() {
        return truckTypesComboBox;
    }

    public JTextField getManufacturingYearTextField() {
        return manufacturingYearTextField;
    }

    public JTextField getRegistrationNumberTextField() {
        return registrationNumberTextField;
    }

    public JTextField getCity1TextField() {
        return city1TextField;
    }

    public JTextField getCity2TextField() {
        return city2TextField;
    }

    public JTextField getDistanceTextField() {
        return distanceTextField;
    }

    public JTextField getYourBidTextField() {
        return yourBidTextField;
    }

    public JTextField getShippingRouteC1TextField() {
        return shippingRouteC1TextField;
    }

    public JTextField getShippingRouteC2TextField() {
        return shippingRouteC2TextField;
    }

    public JTextField getRequireRefrigeratedTransportYesTextField() {
        return requireRefrigeratedTransportYesTextField;
    }

    public JTextField getShippingWeightTextField() {
        return shippingWeightTextField;
    }

    public JButton getAddRouteButton() {
        return addRouteButton;
    }

    public JTextField getScheduleShippingID() {
        return scheduleShippingID;
    }

    public JButton getScheduleShippingButton() {
        return scheduleShippingButton;
    }
}

