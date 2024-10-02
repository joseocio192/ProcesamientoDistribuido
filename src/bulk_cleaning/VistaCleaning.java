package bulk_cleaning;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import crud.ComponenteHeader;
import layouts.ContentLayout;

public class VistaCleaning extends JPanel implements ComponentListener {

    private JPanel panelContent;

    private JButton btnCleanData;
    private JButton btnInsertDataH;
    private JButton btnInsertDataD;

    private ComponenteHeader componenteHeader;

    public VistaCleaning() {
        initInterface();
        this.addComponentListener(this);
    }

    private void initInterface() {
        setLayout(null);

        componenteHeader = new ComponenteHeader("Bulk Cleaning");
        add(componenteHeader, BorderLayout.NORTH);

        panelContent = new JPanel();
        panelContent.setLayout(new FlowLayout());
        panelContent.setBackground(Color.WHITE);

        btnCleanData = new JButton("Limpiar datos (CSV)");
        panelContent.add(btnCleanData);
        btnInsertDataH = new JButton("Reiniciar datos (H)");
        panelContent.add(btnInsertDataH);
        btnInsertDataD = new JButton("Reiniciar datos (D)");
        panelContent.add(btnInsertDataD);

        add(panelContent);

        setVisible(true);
    }

    public JButton getBtnCleanData() {
        return btnCleanData;
    }

    public JButton getBtnInsertDataH() {
        return btnInsertDataH;
    }

    public JButton getBtnInsertDataD() {
        return btnInsertDataD;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        short w = (short) getWidth();
        short h = (short) getHeight();

        componenteHeader.setBounds(0, 0, getWidth(), (short) (getHeight() * .2));

        panelContent.setBounds(componenteHeader.getX(), componenteHeader.getY() + componenteHeader.getHeight(), w,
                h - componenteHeader.getHeight());
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // This is not used
    }

    @Override
    public void componentShown(ComponentEvent e) {
        // This is not used
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // This is not used
    }

}
