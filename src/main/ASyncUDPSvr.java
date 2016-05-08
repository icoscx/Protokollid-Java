package main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.Iterator;

public class ASyncUDPSvr {
	
	 static int BUF_SZ = 100;

	    class Con {
	        ByteBuffer req;
	        ByteBuffer resp;
	        SocketAddress sa;

	        public Con() {
	            req = ByteBuffer.allocate(BUF_SZ);
	        }
	    }

	    static int port = 1023;
	    private void process() {
	        try {
	            Selector selector = Selector.open();
	            DatagramChannel channel = DatagramChannel.open();
	            InetSocketAddress isa = new InetSocketAddress(port);
	            channel.socket().bind(isa);
	            channel.configureBlocking(false);
	            SelectionKey clientKey = channel.register(selector, SelectionKey.OP_READ);
	            clientKey.attach(new Con());
	            while (true) {
	                try {
	                    selector.select();
	                    Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
	                    while (selectedKeys.hasNext()) {
	                        try {
	                            SelectionKey key = (SelectionKey) selectedKeys.next();
	                            selectedKeys.remove();

	                            if (!key.isValid()) {
	                              continue;
	                            }

	                            if (key.isReadable()) {
	                                read(key);
	                                key.interestOps(SelectionKey.OP_WRITE);
	                            } else if (key.isWritable()) {
	                                write(key);
	                                key.interestOps(SelectionKey.OP_READ);
	                            }
	                        } catch (IOException e) {
	                            System.err.println("glitch, continuing... " +(e.getMessage()!=null?e.getMessage():""));
	                        }
	                    }
	                } catch (IOException e) {
	                    System.err.println("glitch, continuing... " +(e.getMessage()!=null?e.getMessage():""));
	                }
	            }
	        } catch (IOException e) {
	            System.err.println("network error: " + (e.getMessage()!=null?e.getMessage():""));
	        }
	    }

	    private void read(SelectionKey key) throws IOException {
	        DatagramChannel chan = (DatagramChannel)key.channel();
	        Con con = (Con)key.attachment();
	        /**
	         *  the datagram is copied into the given byte buffer con.req
				and its source address is returned con.sa.
	         */
	        con.sa = chan.receive(con.req);
	        System.out.println(new String(con.req.array(), "ASCII"));
	        con.req.rewind();
	        con.resp = Charset.forName( "ASCII" ).newEncoder().encode(CharBuffer.wrap("send the same string"));
	    }

	    private void write(SelectionKey key) throws IOException {
	        DatagramChannel chan = (DatagramChannel)key.channel();
	        Con con = (Con)key.attachment();
	        chan.send(con.resp, con.sa);
	    }

	    static public void main(String[] args) {
	        ASyncUDPSvr svr = new ASyncUDPSvr();
	        svr.process();
	    }
}
