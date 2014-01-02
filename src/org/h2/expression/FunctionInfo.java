/*
 * Copyright 2004-2013 H2 Group. Multiple-Licensed under the H2 License,
 * Version 1.0, and under the Eclipse Public License, Version 1.0
 * (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.expression;

import org.h2.value.DataType;

/**
 * This class contains information about a built-in function.
 */
class FunctionInfo { //7���ֶ�

    /**
     * The name of the function.
     */
    String name;

    /**
     * The function type.
     */
    int type;

    /**
     * The data type of the return value.
     */
    int dataType;

    /**
     * The number of parameters.
     */
    int parameterCount;
    
    //ע�Ͷ��˸�if�����������ʾ: ֻҪ���������Ĳ�����һ����null����ô��������ֵ����null
    /**
     * If the result of the function is NULL if any of the parameters is NULL.
     */
    boolean nullIfParameterIsNull;

    /**
     * If this function always returns the same value for the same parameters.
     */
    boolean deterministic; //������ͬ�Ĳ�����ÿ�ε��ÿ��ܷ��ز�ֵͬ������:CURRENT_TIME��RAND֮��ĺ���

    /**
     * Whether the function is fast, meaning the result shouldn't be cached.
     */
	boolean fast;

	public String toString() { //�Ҽ��ϵ�
//		return name + " type=" + type + " dataType="
//				+ DataType.getDataType(dataType).name + " parameterCount="
//				+ parameterCount + " nullIfParameterIsNull="
//				+ nullIfParameterIsNull + " deterministic=" + deterministic
//				+ " fast=" + fast;
		
		return pad(name, 20) + pad(type, 10) + pad(DataType.getDataType(dataType).name,15) + pad(parameterCount, 6)
				+ pad(nullIfParameterIsNull, 8) + pad(deterministic, 8) + pad(fast, 8);
	}

	public int hashCode() { //�Ҽ��ϵ�
		return type;
	}

	public static String pad(Object string, int n) {
		return org.h2.util.StringUtils.pad(string.toString(), n, " ", true);
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof FunctionInfo))
			return false;
		FunctionInfo fi = (FunctionInfo) obj;
		return !name.equals(fi.name)
				&& 
				type == fi.type //
				&& dataType == fi.dataType
				&& parameterCount == fi.parameterCount //
				&& nullIfParameterIsNull == fi.nullIfParameterIsNull
				&& deterministic == fi.deterministic && fast == fi.fast;
	}


}
