/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.dhruza.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

/**
 *
 * @author dnlbe
 */
public class ReflectionUtils {

    public static void readClassInfo(Class<?> clazz, StringBuilder classInfo) {
        appendPackage(clazz, classInfo);
        appendModifiers(clazz, classInfo);
        appendParent(clazz, classInfo, true);
        appendInterfaces(clazz, classInfo);
    }

    private static void appendPackage(Class<?> clazz, StringBuilder classInfo) {
        classInfo.append("<p>").append(clazz.getPackage()).append("</p>")
                .append("\n\n");
    }

    private static void appendModifiers(Class<?> clazz, StringBuilder classInfo) {

        classInfo.append("<p>").append(Modifier.toString(clazz.getModifiers())).append("</p>")
                .append("<p> </p>").append("<p>").append(clazz.getSimpleName()).append("</p>");
    }

    private static void appendParent(Class<?> clazz, StringBuilder classInfo, boolean first) {
        Class<?> parent = clazz.getSuperclass();
        if (parent == null) {
            return;
        }
        if (first) {
            classInfo.append("<p>").append("\nextends").append("</p>");
        }
        classInfo.append("<p> </p>")
                .append("<p>").append(parent.getName()).append("</p>");
        appendParent(parent, classInfo, false);
    }

    private static void appendInterfaces(Class<?> clazz, StringBuilder classInfo) {
        if (clazz.getInterfaces().length > 0) {
            classInfo.append("<p>").append("\nimplements").append("</p>");
        }
        for (Class<?> in : clazz.getInterfaces()) {
            classInfo.append("<p> </p>")
                    .append("<p>").append(in.getName()).append("</p>");
        }
    }

    public static void readClassAndMembersInfo(Class<?> clazz, StringBuilder classAndMembersInfo) {
        readClassInfo(clazz, classAndMembersInfo);
        appendFields(clazz, classAndMembersInfo);
        appendMethods(clazz, classAndMembersInfo);
        appendConstructors(clazz, classAndMembersInfo);
    }
 
    private static void appendFields(Class<?> clazz, StringBuilder classAndMembersInfo) {
        //Field[] fields = clazz.getFields(); // returns public and inherited
        Field[] fields = clazz.getDeclaredFields(); // returns public, protected, default (package) access, and private fields, but excludes inherited fields
        classAndMembersInfo.append("\n\n").append("<h5>Fields:</h5>").append("\n");
        for (Field field : fields) {
            classAndMembersInfo
                    .append("<p>").append(field).append("</p>")
                    .append("\n");
        }
    }

    private static void appendMethods(Class<?> clazz, StringBuilder classAndMembersInfo) {
        Method[] methods = clazz.getDeclaredMethods();
        classAndMembersInfo.append("<h5>Methods:</h5>").append("\n");
        for (Method method : methods) {
            classAndMembersInfo.append("\n");
            appendMethodAnnotations(method, classAndMembersInfo);
            classAndMembersInfo
                    .append("<p>").append(Modifier.toString(method.getModifiers())).append(" ")
                    .append(method.getReturnType()).append(" ")
                    .append(method.getName());
            appendParameters(method, classAndMembersInfo);
            appendExceptions(method, classAndMembersInfo);
            classAndMembersInfo.append(" </p>");
            classAndMembersInfo.append("<p></p>");
        }
        classAndMembersInfo.append("\n");
    }

    private static void appendMethodAnnotations(Executable executable, StringBuilder classAndMembersInfo) {
        for (Annotation annotation : executable.getAnnotations()) {
            classAndMembersInfo
                    .append("<p>").append(annotation).append("</p>")
                    .append("\n");
        }
    }

    private static void appendParameters(Executable executable, StringBuilder classAndMembersInfo) {
        classAndMembersInfo.append("(");
        for (Parameter parameter : executable.getParameters()) {
            classAndMembersInfo
                    .append(parameter)
                    .append(", ");
        }
        if (classAndMembersInfo.toString().endsWith(", ")) {
            classAndMembersInfo.delete(classAndMembersInfo.length() - 2, classAndMembersInfo.length());
        }
        classAndMembersInfo.append(")");
    }

    private static void appendExceptions(Executable executable, StringBuilder classAndMembersInfo) {
        Class<?>[] exceptionTypes = executable.getExceptionTypes();
        if (exceptionTypes.length > 0) {
            classAndMembersInfo.append("<p>").append(" throws ").append("</p>");
            for (Class<?> exceptionType : exceptionTypes) {
                classAndMembersInfo
                        .append("<p>").append(exceptionType).append("</p>");
            }
            if (classAndMembersInfo.toString().endsWith(", ")) {
                classAndMembersInfo.delete(classAndMembersInfo.length() - 2, classAndMembersInfo.length());
            }
        }
    }

    private static void appendConstructors(Class<?> clazz, StringBuilder classAndMembersInfo) {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        classAndMembersInfo.append("<h5>Constructors:</h5>").append("\n");
        for (Constructor constructor : constructors) {
            classAndMembersInfo.append("\n");
            appendMethodAnnotations(constructor, classAndMembersInfo);
            classAndMembersInfo
                    .append("<p>").append(Modifier.toString(constructor.getModifiers()))
                    .append(" ").append(constructor.getName());
            appendParameters(constructor, classAndMembersInfo);
            appendExceptions(constructor, classAndMembersInfo);
            classAndMembersInfo.append("</p>");
        }
    }

}
