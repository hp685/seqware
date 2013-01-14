package net.sourceforge.seqware.metadb.util;

/*
 * Copyright (C) 2013 SeqWare
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This is copied from seqware-common.
 * Technically, we could have added a dependency, but I didn't want to run the risk of further worsening our growing jar problem.
 * @author dyuen
 */
public class DBUtils {
    public static boolean closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException se) {
            System.out.println("We got an exception while trying to close connections ");
            se.printStackTrace();
            return true;
        }
        return false;
    }

    public static boolean closeStatement(Statement s) {
        try {
            if (s != null) {
                s.close();
            }
        } catch (SQLException se) {
            System.out.println("We got an exception while trying to close connections ");
            se.printStackTrace();
            return true;
        }
        return false;
    }

    public static boolean closeConnection(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException se) {
            System.out.println("We got an exception while trying to close connections ");
            se.printStackTrace();
            return true;
        }
        return false;
    }
}
