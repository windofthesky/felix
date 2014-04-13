/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package dm.annotation.plugin.bnd;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class containings pattern matching helper methods.
 * 
 * @author <a href="mailto:dev@felix.apache.org">Felix Project Team</a>
 */
public class Patterns
{
    // Pattern used to check if a method is void and does not take any params
    public final static Pattern VOID = Pattern.compile("\\(\\)V");

    // Pattern used to check if a method returns an array of Objects
    public final static Pattern COMPOSITION = Pattern.compile("\\(\\)\\[Ljava/lang/Object;");

    // Pattern used to parse the class parameter from the bind methods ("bind(Type)" or "bind(Map, Type)" or "bind(BundleContext, Type)"
    public final static Pattern BIND_CLASS = Pattern.compile("\\((L[^;]+;)?L([^;]+);\\)V");

    // Pattern used to parse classes from class descriptors;
    public final static Pattern CLASS = Pattern.compile("L([^;]+);");
    
    // Pattern used to parse the field on which a Publisher annotation may be applied on
    public final static Pattern RUNNABLE = Pattern.compile("Ljava/lang/Runnable;");
    
    // Pattern used to parse a field whose type is BundleContext
    public final static Pattern BUNDLE_CONTEXT = Pattern.compile("Lorg/osgi/framework/BundleContext;");

    // Pattern used to parse a field whose type is DependencyManager
    // TODO change package name
    public final static Pattern DEPENDENCY_MANAGER = Pattern.compile("Ldm.DependencyManager;");
    
    // Pattern used to parse a field whose type is Component
    // TODO change package name
    public final static Pattern COMPONENT = Pattern.compile("Ldm.Component;");

    /**
     * Parses a class.
     * @param clazz the class to be parsed (the package is "/" separated).
     * @param pattern the pattern used to match the class.
     * @param group the pattern group index where the class can be retrieved.
     * @return the parsed class.
     */
    public static String parseClass(String clazz, Pattern pattern, int group)
    {
        Matcher matcher = pattern.matcher(clazz);
        if (matcher.matches())
        {
            return matcher.group(group).replace("/", ".");
        }
        else
        {
            throw new IllegalArgumentException("Invalid class descriptor: " + clazz);
        }
    }
    
    /**
     * Checks if a method descriptor matches a given pattern. 
     * @param the method whose signature descriptor is checked
     * @param pattern the pattern used to check the method signature descriptor
     * @throws IllegalArgumentException if the method signature descriptor does not match the given pattern.
     */
    public static void parseMethod(String method, String descriptor, Pattern pattern)
    {
        Matcher matcher = pattern.matcher(descriptor);
        if (!matcher.matches())
        {
            throw new IllegalArgumentException("Invalid method " + method + ", wrong signature: "
                + descriptor);
        }
    }
    
    /**
     * Checks if a field descriptor matches a given pattern.
     * @param field the field whose type descriptor is checked
     * @param descriptor the field descriptor to be checked
     * @param pattern the pattern to use
     * @throws IllegalArgumentException if the method signature descriptor does not match the given pattern.
     */
    public static void parseField(String field, String descriptor, Pattern pattern) {
        Matcher matcher = pattern.matcher(descriptor);
        if (!matcher.matches())
        {
            throw new IllegalArgumentException("Invalid field " + field + ", wrong signature: "
                + descriptor);
        }
    }
}
