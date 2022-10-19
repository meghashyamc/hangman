package main.java;

import asg.cliche.ShellFactory;
import java.io.IOException;

public class Main {

	public static final String APPNAME = "hangman";
	public static final String PROMPTNAME = "hello";

	public static void main(String[] args) throws IOException {

		Shell shell = new Shell();
		ShellFactory.createConsoleShell(PROMPTNAME, APPNAME, shell).commandLoop();

	}
}
