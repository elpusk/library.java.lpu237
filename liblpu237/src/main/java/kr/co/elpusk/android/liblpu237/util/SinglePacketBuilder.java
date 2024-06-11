package kr.co.elpusk.android.liblpu237.util;

public class SinglePacketBuilder {
    private int m_n_offset = -1;
    private byte[] m_single_packet = null;
    private boolean m_b_error = true;
    private boolean m_complete_build = false;

    public boolean IsComplete(){
        return m_complete_build;
    }
    public boolean IsError(){
        return m_b_error;
    }
    public int Size(){
        if(m_single_packet == null)
            return 0;
        return m_single_packet.length;
    }
    public byte[] GetSinglePacketBuilder(){
        return m_single_packet;
    }
    public byte[] GetSinglePacketBuilderWithTripleE6(){
        byte[] triplE6 = new byte[]{(byte)0xE6,(byte)0xE6,(byte)0xE6};
        byte[] out = null;
        if(m_single_packet!=null){
            out = new byte[triplE6.length+m_single_packet.length];
            System.arraycopy(triplE6,0,out,0,triplE6.length);
            System.arraycopy(m_single_packet,0,out,triplE6.length,m_single_packet.length);
        }
        return out;
    }

    public SinglePacketBuilder(byte[] sFirstPacket){
        m_b_error = true;
        m_complete_build = true;

        do{
            if(sFirstPacket==null){
                continue;
            }
            if(sFirstPacket.length<3){
                continue;
            }
            if(Tools.compare_byte(ParsingTag.CONST_TAG_E6,sFirstPacket[0])!=0){
                continue;
            }
            if(Tools.compare_byte(ParsingTag.CONST_TAG_E6,sFirstPacket[1])!=0){
                continue;
            }
            if(Tools.compare_byte(ParsingTag.CONST_TAG_E6,sFirstPacket[2])!=0){
                continue;
            }

            if(sFirstPacket.length<(3+4) ){
                continue;
            }

            int n_offset = 3;
            byte[] four = new byte[4];
            System.arraycopy(sFirstPacket,n_offset, four,0,four.length);
            int n_len= Tools.get_unsigned_short_from_4bytes_little_endian(four);

            if(n_len<0){
                continue;
            }
            if(n_len==0){
                m_b_error = false;
                m_complete_build = true;
                continue;
            }
            m_single_packet = new byte[n_len+four.length];
            int n_cpy = sFirstPacket.length-n_offset;
            if( n_cpy > m_single_packet.length){
                n_cpy = m_single_packet.length;
            }
            m_n_offset = 0;
            System.arraycopy(sFirstPacket,n_offset, m_single_packet,m_n_offset,n_cpy);
            m_n_offset += n_cpy;

            if(m_n_offset>= m_single_packet.length){
                m_complete_build = true;
            }
            else{
                m_complete_build = false;//need more
            }
            m_b_error = false;

        }while(false);

    }

    public boolean append(byte[] sPacket){

        do{
            if(m_b_error){
                continue;
            }
            if(m_complete_build){
                continue;
            }
            if(sPacket == null){
                continue;
            }
            if(Tools.compare_byte(ParsingTag.CONST_TAG_E6,sPacket[0])!=0){
                m_b_error = true;
                m_complete_build = true;
                continue;
            }
            if(Tools.compare_byte(ParsingTag.CONST_TAG_E6,sPacket[1])!=0){
                m_b_error = true;
                m_complete_build = true;
                continue;
            }
            if(Tools.compare_byte(ParsingTag.CONST_TAG_E6,sPacket[2])!=0){
                m_b_error = true;
                m_complete_build = true;
                continue;
            }

            int n_cpy = sPacket.length-3;
            if( n_cpy > (m_single_packet.length-m_n_offset)){
                n_cpy = m_single_packet.length-m_n_offset;
            }

            System.arraycopy(sPacket,3, m_single_packet,m_n_offset,n_cpy);
            m_n_offset += n_cpy;

            if(m_n_offset>= m_single_packet.length){
                m_complete_build = true;
            }

        }while(false);
        return m_complete_build;
    }

    private SinglePacketBuilder(){

    }

}
