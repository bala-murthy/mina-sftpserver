package com.protocols.sftp;

import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.UserAuth;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.UserAuthPasswordFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.scp.ScpCommandFactory;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MinaSftpServer {

    public static void main(String[] args) {


        SshServer sftpServer = SshServer.setUpDefaultServer();
        sftpServer.setPort(2121);
        sftpServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(new File("hostkey-new.ser")));

        // Needed for Password based authentication. Hardcoded for testing at the moment.

        sftpServer.setPasswordAuthenticator(new PasswordAuthenticator() {
            public boolean authenticate(String username, String password, ServerSession session) {
                return "bala".equals(username) && "123".equals(password);
            }
        });

        // Command Factory enabled to ensure that the server accepts and executes SFTP commands.
        // without this, you will be able to login but unable to see the directory listing
        // and unable to put or get files !

        sftpServer.setCommandFactory(new ScpCommandFactory());
        List<NamedFactory<Command>> namedFactoryList = new ArrayList<NamedFactory<Command>>();
        namedFactoryList.add(new SftpSubsystemFactory());
        sftpServer.setSubsystemFactories(namedFactoryList);


        try {
            sftpServer.start();
            //Keep this on loop. Use IDE 'STOP' option to shutdown the server.
            while (true) ;
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}


