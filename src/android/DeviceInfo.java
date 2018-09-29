/**
 * author :  lipan
 * filename :  PhoneInfo.java
 * create_time : 2014年8月31日 下午9:16:18
 */
 
package org.apache.cordova.aspolarsignalstrength;
/**
 * @author : lipan
 * @create_time : 2014年8月31日 下午9:16:18
 * @desc : 手机信息
 * @update_person:
 * @update_time :
 * @update_desc :
 *
 */
 
public class DeviceInfo
{
    public String phone_nbr = ""; // Número de teléfono móvil
    public String phone_type; // Modelo de teléfono
    public String carrier; // Operador: CUC, CMC, CTC
    public String carrier_name; // Operador: CUC, CMC, CTC
    public String net_type; // Tipo de red: 2G, 3G, 4G
    @Override
    public  String toString()
    {
        return DeviceInfo [phone_nbr= + phone_nbr + , phone_type= + phone_type + , carrier=
                + carrier + , net_type= + net_type + ];
    }
    
}