package org.h2.util;

import org.h2.expression.Expression;

public class ColumnNamer {
 
    /**
     * Create a standardized column name that isn't null and doesn't have a CR/LF in it.
     * @param columnExp the column expression
     * @param indexOfColumn index of column in below array
     * @return
     */
    public static String getColumnName(Expression expr, int indexOfColumn) {
        return getColumnName(expr,indexOfColumn,(String) null);
    }
    /**
     * Create a standardized column name that isn't null and doesn't have a CR/LF in it.
     * @param columnExp the column expression
     * @param indexOfColumn index of column in below array
     * @param columnNameOverides array of overriding column names
     * @return the new column name
     */
    public static String getColumnName(Expression columnExp, int indexOfColumn, String[] columnNameOverides){
        String columnNameOverride = null;
        if (columnNameOverides != null && columnNameOverides.length > indexOfColumn){
            columnNameOverride = columnNameOverides[indexOfColumn];
        }  
        return getColumnName(columnExp, indexOfColumn, columnNameOverride);
    } 
    
    /**
     * Create a standardized column name that isn't null and doesn't have a CR/LF in it.
     * @param columnExp the column expression
     * @param indexOfColumn index of column in below array
     * @param columnNameOverride single overriding column name
     * @return the new column name
     */
    public static String getColumnName(Expression columnExp, int indexOfColumn, String columnNameOverride) {
        // try a name from the column name override
        String columnName = null;
        if (columnNameOverride != null){
            columnName = columnNameOverride;
            //if(!isAllowableColumnName(columnName)){
            //    columnName = columnName.replace("\n", " ").replace("\r", " ");
            //}            
            //if(!isAllowableColumnName(columnName)){
            //    columnName = null;
            //}
            return columnName;
        }  
        // try a name from the column alias
        if (columnName==null && columnExp.getAlias()!=null){
            columnName = columnExp.getAlias();
            if(!isAllowableColumnName(columnName)){
                columnName = columnName.replace("\n", " ").replace("\r", " ");
            }
            if(!isAllowableColumnName(columnName)){
                columnName = null;
            }
        }
        // try a name derived from the column expression SQL
        if (columnName==null && columnExp.getColumnName()!=null){
             columnName =  columnExp.getColumnName();
             if(!isAllowableColumnName(columnName)){
                 columnName = columnName.replace("\n", " ").replace("\r", " ");
             }
             if(!isAllowableColumnName(columnName)){
                 columnName = null;
             }
        }
        // try a name derived from the column expression plan SQL
        if (columnName==null && columnExp.getSQL()!=null){
             columnName =  columnExp.getSQL();
             if(!isAllowableColumnName(columnName)){
                 columnName = columnName.replace("\n", " ").replace("\r", " ");
             }
             if(!isAllowableColumnName(columnName)){
                 columnName = null;
             }
        }
        // go with a innocuous default name pattern
        if (columnName==null){
            columnName =  "_unnamed_column_"+(indexOfColumn+1)+"_";
       }
        return columnName;
    }
        
    public static boolean isAllowableColumnName(String proposedName){
        if (proposedName == null){
            return false;
        }
        //if(proposedName.contains("\n") || proposedName.contains("\r")){
        //    return false;
        //}
        return true;
    }

}
