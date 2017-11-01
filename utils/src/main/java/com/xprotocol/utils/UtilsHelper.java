/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.utils;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import com.xprotocol.utils.exceptions.UUIDStringConversionException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author zhao0677
 */
public class UtilsHelper {
    
    static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 0x01b21dd213814000L;
    
    public static UUID getUUIDBasedOnTime(){
        return UUIDs.timeBased();
    }
    
    public static UUID getUUIDBasedOnEthernet() {
        NoArgGenerator timeBasedGenerator = Generators.timeBasedGenerator();
        timeBasedGenerator = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
        return timeBasedGenerator.generate();
    }
    
    /**
     * From https://gist.github.com/jeffjohnson9046/c663dd22bbe6bb0b3f5e
     * @param uuid
     * @return 
     */
    public static byte[] getBytesFromUUID(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());

        return bb.array();
    }
    
    /**
     * From https://gist.github.com/jeffjohnson9046/c663dd22bbe6bb0b3f5e
     * @param bytes
     * @return UUID
     */
    public static UUID getUUIDFromBytes(byte[] bytes) {

        return UUID.nameUUIDFromBytes(bytes);
    }
    
    /**
     * From https://gist.github.com/jeffjohnson9046/c663dd22bbe6bb0b3f5e
     * @param UUIDStr
     * @return UUID
     * @throws com.xprotocol.utils.exceptions.UUIDStringConversionException
     */
    public static UUID getUUIDFromString(String UUIDStr) throws UUIDStringConversionException {
        UUID uuid;
        try{
            uuid = UUID.fromString(UUIDStr);
        }
        catch(Exception ex){
            uuid = UUID.fromString(UUIDStringFormat(UUIDStr));
        }
        return uuid;
    }
    
    /**
     * From https://support.datastax.com/hc/en-us/articles/204226019-Converting-TimeUUID-Strings-to-Dates
     * @param uuid
     * @return date
     */
    public static Date getDateFromUUID(UUID uuid) {
        long time = (uuid.timestamp() - NUM_100NS_INTERVALS_SINCE_UUID_EPOCH) / 10000;
        return new Date(time);
    }
    
    public static String UUIDStringFormat(String UUIDStr) throws UUIDStringConversionException{
        String UUIDStr2 = java.util.UUID.fromString(UUIDStr.replaceFirst( 
            "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5")).toString();
        if(Validators.isValidUUIDString(UUIDStr2)){
            return UUIDStr2;
        }
        else{
            throw new UUIDStringConversionException("Cannot convert string: '"+UUIDStr+"' to standard UUID string.");
        }
    }
}
