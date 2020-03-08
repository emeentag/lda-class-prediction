package com.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

import net.miginfocom.swing.MigLayout;

public class GUI extends JPanel {

	private static final long serialVersionUID = -6036898274031088873L;

	private MainFrame parent;
	private JScrollPane outputScrollPane;
	private JTextArea outputTextArea, inputTestX, inputTestY, inputLimitX, inputLimitY, inputNumPoints;
	private JLabel name, labelClass;
	private JButton buttonStart, buttonReset, buttonAdd, buttonAnalysis;

	public GUI() {
	}

	public GUI(JFrame parent) {
		this.parent = (MainFrame) parent;
		makeGUI();
	}

	public GUI(LayoutManager layout) {
		super(layout);
	}

	public GUI(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public GUI(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	private void makeGUI() {
		setLayout(new MigLayout("fillx, insets 5, top, left"));
		setPreferredSize(new Dimension(800, 200));
		setBackground(Color.LIGHT_GRAY);

		createOutPutPanel();
		createPredictionPanel();
	}

	private void createPredictionPanel() {
		name = new JLabel();
		name.setText("Point Information");
		this.add(name, "cell 1 0");

		coordinateAnalyzePanel();
	}

	private void coordinateAnalyzePanel() {
		JPanel coordinateContainer = new JPanel();
		coordinateContainer.setLayout(new MigLayout("fillx, insets 5, top, left"));
		coordinateContainer.setPreferredSize(new Dimension(300, 200));
		this.add(coordinateContainer, "cell 1 1");

		// Test X.
		name = new JLabel();
		name.setText("Test X: ");
		coordinateContainer.add(name, "cell 0 3");

		inputTestX = new JTextArea();
		inputTestX.setPreferredSize(new Dimension(100, 15));
		coordinateContainer.add(inputTestX, "cell 1 3");

		// Test Y.
		name = new JLabel();
		name.setText("Test Y: ");
		coordinateContainer.add(name, "cell 1 3");

		inputTestY = new JTextArea();
		inputTestY.setPreferredSize(new Dimension(100, 15));
		coordinateContainer.add(inputTestY, "cell 2 3");

		// Class must be.
		name = new JLabel();
		name.setText("Class must be: ");
		coordinateContainer.add(name, "cell 0 4");

		labelClass = new JLabel();
		labelClass.setText("--");
		labelClass.setPreferredSize(new Dimension(100, 15));
		coordinateContainer.add(labelClass, "cell 1 4");

		// Num. Points.
		name = new JLabel();
		name.setText("Num. Points: ");
		coordinateContainer.add(name, "cell 0 1");

		inputNumPoints = new JTextArea();
		inputNumPoints.setPreferredSize(new Dimension(53, 15));
		coordinateContainer.add(inputNumPoints, "cell 1 1");

		// Limit X.
		name = new JLabel();
		name.setText("Limit X: ");
		coordinateContainer.add(name, "cell 0 0");

		inputLimitX = new JTextArea();
		inputLimitX.setPreferredSize(new Dimension(100, 15));
		coordinateContainer.add(inputLimitX, "cell 1 0");

		// Limit Y.
		name = new JLabel();
		name.setText("Limit Y: ");
		coordinateContainer.add(name, "cell 1 0");

		inputLimitY = new JTextArea();
		inputLimitY.setPreferredSize(new Dimension(100, 15));
		coordinateContainer.add(inputLimitY, "cell 2 0");

		// Buttons.
		buttonStart = new JButton();
		buttonStart.setText("Start");
		coordinateContainer.add(buttonStart, "cell 0 2");
		buttonStart.addMouseListener(mouseListener);

		buttonReset = new JButton();
		buttonReset.setText("Reset");
		coordinateContainer.add(buttonReset, "cell 1 2");
		buttonReset.setEnabled(false);

		buttonAdd = new JButton();
		buttonAdd.setText("Add");
		coordinateContainer.add(buttonAdd, "cell 0 5");
		buttonAdd.setEnabled(false);

		buttonAnalysis = new JButton();
		buttonAnalysis.setText("Analysis");
		coordinateContainer.add(buttonAnalysis, "cell 1 5");
		buttonAnalysis.setEnabled(false);
	}

	private void createOutPutPanel() {
		name = new JLabel();
		name.setText("Output");
		this.add(name, "cell 0 0");

		Dimension dimension = new Dimension(500, 200);
		outputTextArea = new JTextArea();
		outputTextArea.setWrapStyleWord(true);
		outputTextArea.setLineWrap(true);

		DefaultCaret caret = (DefaultCaret) outputTextArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		outputScrollPane = new JScrollPane(outputTextArea);
		outputScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		outputScrollPane.setPreferredSize(dimension);
		this.add(outputScrollPane, "cell 0 1");
	}

	private MouseListener mouseListener = new MouseListener() {

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			JButton button = (JButton) e.getComponent();

			if (button.getText().equalsIgnoreCase("Add")) {

				int xPos = Integer.valueOf(inputTestX.getText());
				int yPos = Integer.valueOf(inputTestY.getText());
				int limXPos = Integer.valueOf(inputLimitX.getText());
				int limYPos = Integer.valueOf(inputLimitY.getText());

				if (inputTestX.getText().length() <= 0 || inputTestY.getText().length() <= 0) {
					getOutputTextArea().setText("* Please provide coordinate of the point you want to add!\n");
					return;
				}
				if (xPos > (parent.getPlane().getWIDTH() - CirclePoint.WIDTH)
						|| yPos > (parent.getPlane().getHEIGHT() - CirclePoint.HEIGHT)) {
					getOutputTextArea().setText("* Coordinates must be (Xmax:750, Ymax:350).\n");
					return;

				}

				int pointClass = (xPos < limXPos && yPos < limYPos) ? 1 : 2;
				labelClass.setText(String.valueOf(pointClass));

				parent.addPoint(xPos, yPos, pointClass);

			} else if (button.getText().equalsIgnoreCase("Analysis")) {
				if (inputTestX.getText().length() <= 0 || inputTestY.getText().length() <= 0) {
					getOutputTextArea().setText("* Please add a test point first!" + "\n");
					return;
				}
				parent.analysis();
			} else if (button.getText().equalsIgnoreCase("Start")) {

				int limXPos = Integer.valueOf(inputLimitX.getText());
				int limYPos = Integer.valueOf(inputLimitY.getText());

				if (inputLimitX.getText().length() <= 0 || inputLimitY.getText().length() <= 0
						|| inputNumPoints.getText().length() <= 0) {
					getOutputTextArea().setText("* Please provide Limit X, Y and Num. Points!" + "\n");
					return;
				}

				if (limXPos > (parent.getPlane().getWIDTH() - CirclePoint.WIDTH)
						|| limYPos > (parent.getPlane().getHEIGHT() - CirclePoint.HEIGHT)) {
					getOutputTextArea().setText("* Coordinates must be (Xmax:750, Ymax:350).\n");
					return;

				}

				buttonStart.setEnabled(false);
				buttonStart.removeMouseListener(mouseListener);

				buttonAdd.setEnabled(true);
				buttonAdd.addMouseListener(mouseListener);
				buttonAnalysis.setEnabled(true);
				buttonAnalysis.addMouseListener(mouseListener);
				buttonReset.setEnabled(true);
				buttonReset.addMouseListener(mouseListener);
				parent.start(Integer.valueOf(inputLimitX.getText()), Integer.valueOf(inputLimitY.getText()),
						Integer.valueOf(inputNumPoints.getText()));
			} else if (button.getText().equalsIgnoreCase("Reset")) {
				parent.reset();
			}
		}
	};

	public JTextArea getOutputTextArea() {
		return outputTextArea;
	}

	public void setOutputTextArea(JTextArea outputTextArea) {
		this.outputTextArea = outputTextArea;
	}

	public void reset() {
		getOutputTextArea().setText("");
		inputLimitX.setText("");
		inputLimitY.setText("");
		labelClass.setText("--");
		inputTestX.setText("");
		inputTestY.setText("");
		inputNumPoints.setText("");
		buttonStart.setEnabled(true);
		buttonStart.addMouseListener(mouseListener);

		buttonAdd.setEnabled(false);
		buttonAdd.removeMouseListener(mouseListener);
		buttonAnalysis.setEnabled(false);
		buttonAnalysis.removeMouseListener(mouseListener);
		buttonReset.setEnabled(false);
		buttonReset.removeMouseListener(mouseListener);
	}

}
