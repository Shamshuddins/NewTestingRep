package main.java.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.fasterxml.jackson.dataformat.xml.annotation.*;

@JacksonXmlRootElement(localName = "suite")
public class Suite {

	@JacksonXmlProperty(isAttribute = true)
	private String name;

	@JacksonXmlProperty(localName = "test")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Test> tests;

	public Suite(String name) {
		this.name = name;
		this.tests = new ArrayList<Suite.Test>();
	}

	public void addTest(String testname, String paramName, String paramValue, String className) {
		Test test = new Test(testname);
		// test.addParam(paramName, paramValue);
		Pattern.compile(",").splitAsStream(className).forEach(test::addClass);
		this.tests.add(test);
	}

	class Test {

		@JacksonXmlProperty(isAttribute = true)
		private String name;

		// @JacksonXmlProperty(localName = "parameter")
		// private Parameter param;

		@JacksonXmlProperty(localName = "classes")
		private Classes klasses;

		public Test(String name) {
			this.name = name;
			klasses = new Classes();
		}

		public void addClass(String name) {
			klasses.assClasses(name);
		}

	}

	class Classes {

		@JacksonXmlProperty(localName = "class")
		@JacksonXmlElementWrapper(useWrapping = false)
		private List<Class> classes;

		public Classes() {
			this.classes = new ArrayList<Suite.Class>();
		}

		public void assClasses(String name) {
			Class newClass = new Class(name);
			this.classes.add(newClass);

		}
	}

	class Class {

		@JacksonXmlProperty(isAttribute = true)
		private String name;

		private Components components = new Components();

		Class(String name) {
			this.name = name;
		}

		public void addComponent(String name) {
			components.assComponents("openURL");
		}
	}

	class Components {

		@JacksonXmlProperty(localName = "methods")
		Component component2 = new Component("openURL");
		// @JacksonXmlElementWrapper(useWrapping = false)
		private List<Component> components1;

		public Components() {
			this.components1 = new ArrayList<Suite.Component>();
		}

		public void assComponents(String name) {
			this.components1.add(new Component(name));
		}
	}

	class Component {

		// @JacksonXmlProperty(localName = "include")
		private String name;

		Component(String name) {
			this.name = "openURl";
		}

	}

}