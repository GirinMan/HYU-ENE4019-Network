import socket

BUFFER = 4096
connected_before = False
HOST = '127.0.0.1'
print('Host IP: '+HOST)
PORT = 50000
print('PORT: '+str(PORT))

while True:
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.connect((HOST, PORT))
    if not connected_before:
        print('Server connection enabled!\n\n\r')
        connected_before = True

    msg = str(input('Your message to server> '))  
    sock.send(msg.encode())
    if msg == '#exit':
        break

    recv = sock.recv(BUFFER)
    msg_recv = str(recv.decode('cp949'))
    print('TCP Server said: %s\n' % msg_recv)

sock.close()

input('\nPress Any Key to Continue...')
