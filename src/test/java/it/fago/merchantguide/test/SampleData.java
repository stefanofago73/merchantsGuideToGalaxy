package it.fago.merchantguide.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class SampleData {

	public static final InputStream from(String data) {
		try {
			return new ByteArrayInputStream(data.getBytes("ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Error creating stream!", e);
		}
	}

	public static String numeralSet1() {
		return "glob means I\n" + "prok means V\n" + "pish means X\n" + "tegj means L\n";
	}

	public static String numeralSet2() {
		return "glob means V\n" + "prok means I\n" + "pish means L\n";
	}

	public static String numeralSet3() {
		return "glob means D\n" + "prok means M\n" + "pish means L\n" + "tegj means C\n" + "poar means I\n";
	}

	public static String numeralsCorruptedAtTheStart() {
		return "glob means \n" + "prok means M\n" + "pish means L\n" + "tegj means C\n";
	}

	public static String numeralsCorruptedAtTheEnd() {
		return "glob means D\n" + "prok means M\n" + "pish means L\n" + "tegj means \n";
	}

	public static String numeralEmpty() {
		return "";
	}

	public static String complete1() {
		return "glob means I\n" + "prok means V\n" + "pish means X\n" + "tegj means L\n"
				+ "glob glob units of Silver are worth 34 Credits\n"
				+ "glob prok units of Gold are worth 57800 Credits\n"
				+ "pish pish units of Iron are worth 3910 Credits\n" + "how much is pish tegj glob glob ?\n"
				+ "how many Credits is glob prok Silver ?\n" + "how many Credits is glob prok Gold ?\n"
				+ "how many Credits is glob prok Iron ?\n"
				+ "how much wood could a woodchuck chuck if a woodchuck could chuck wood ?";
	}

	public static String completeCorrupted() {
		return "glob means I\n" + "prok means V\n" + "tegj means L\n"
				+ "glob glob units of Silver are worth 34 Credits\n"
				+ "glob prok units of Gold are worth 57800 Credits\n"
				+ "pish pish units of Iron are worth 3910 Credits\n" + "how much is pish tegj glob glob ?\n"
				+ "how many Credits is glob prok Silver ?\n" + "how many Credits is glob prok Gold ?\n"
				+ "how many Credits is glob prok Iron ?\n"
				+ "how much wood could a woodchuck chuck if a woodchuck could chuck wood ?";
	}

	public static String completeEmpty() {
		return "";
	}

}// END