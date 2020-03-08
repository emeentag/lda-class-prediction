package com.utils;

import javax.swing.JFrame;

public class Utils {

	public Utils() {

	}

	public static void setJFrameDefaults(JFrame frame) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}

}
