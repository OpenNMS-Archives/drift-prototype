/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2017-2017 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2017 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.drift.converter;

import java.util.Arrays;

public class IntegerConverter {

    public Integer convert(byte[] input) {
        if (input.length == 1) {
            return input[0] & 0xFF;
        } else if (input.length == 2) {
            int result = input[1] & 0xFF;
            result |= ((input[0] << 8) & 0xFF00);
            return result;
        } else if(input.length == 3) {
            int result  = input[2] & 0xFF;
            result |= ((input[1] << 8) & 0xFF00);
            result |= ((input[0] << 16) & 0xFF0000);
            return result;
        } else if (input.length == 4) {
            int result  = input[3] & 0xFF;
            result |= ((input[2] << 8) & 0xFF00);
            result |= ((input[1] << 16) & 0xFF0000);
            result |= ((input[0] << 24) & 0xFF000000);
            return result;
        }
        throw new IllegalArgumentException("Cannot convert " + Arrays.toString(input) + " to int");
    }
}
