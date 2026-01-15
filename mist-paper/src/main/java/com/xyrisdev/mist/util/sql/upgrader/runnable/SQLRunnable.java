/**
 * External utility class.
 * Maintained by Mist.
 */
package com.xyrisdev.mist.util.sql.upgrader.runnable;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLRunnable {
	void run() throws SQLException;
}
