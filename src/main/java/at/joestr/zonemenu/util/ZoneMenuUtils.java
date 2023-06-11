//
// MIT License
//
// Copyright (c) 2017-2023 Joel Strasser <joelstrasser1@gmail.com>
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
package at.joestr.zonemenu.util;

/**
 *
 * @author joestr
 */
public class ZoneMenuUtils {

  public static String RegionFormatZone = "__zonemenu__{creator}+{count}";
  public static String RegionFormatSubZone = "{parent}-{count}";

  public static String createRegionNameFor(String playerName, int count) {
    return ZoneMenuUtils.RegionFormatZone.replace("{creator}", playerName).replace("{count}", "" + count);
  }

  public static String createSubregionNameFor(String parent, int count) {
    return ZoneMenuUtils.RegionFormatSubZone.replace("{parent}", parent).replace("{count}", "" + count);
  }

  public static String regionToZoneName(String regionName) {
    return regionName.replace('+', '#').replace('-', '.').replace("__zonemenu__", "");
  }

  public static String zoneToRegionName(String zoneName) {
    return "__zonemenu__" + zoneName.replace('#', '+').replace('.', '-');
  }
}
