import socket

HOST = '127.0.0.1'
PORT = 50000
BUFFER = 4096
server_enabled = True

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.bind((HOST, PORT))
sock.listen(0)
print('TCP Server listens at: %s:%s\n-----------------------------\r' % (HOST, PORT))

while True:
    if not server_enabled:
        input('\nPress Any Key to Continue...')
        break
    
    print('\nWating for connection with Client...\n')
    client_sock, client_addr = sock.accept()

    while True:
        recv = client_sock.recv(BUFFER)
        if not recv:
            client_sock.close()
            break
        
        msg_recv = str(recv.decode('cp949'))
        if msg_recv == '#exit':
            print('Client was disconnected from connection')
            server_enabled = False
        else:
            print('Client %s:%s said: %s' % (client_addr[0], client_addr[1], msg_recv))
            msg = str(input('Your message to the Client> '))
            client_sock.send(msg.encode())

sock.close()


