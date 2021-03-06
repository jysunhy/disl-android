// Definition of the Socket class

#ifndef _SOCKET_H_
#define _SOCKET_H_


#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <unistd.h>
#include <arpa/inet.h>

//#define DEBUG_MODE

const int MAXHOSTNAME = 200;
const int MAXCONNECTIONS = 5;
const int MAXRECV = 500;
//const int MSG_NOSIGNAL = 0; // defined by dgame

class Socket
{
 public:
  Socket(bool need = false);
  virtual ~Socket();

  // Server initialization
  bool create();
  bool bind ( const int port );
  bool listen() const;
  bool accept ( Socket& ) const;

  // Client initialization
  //bool connect ( const std::string host, const int port );
  bool Connect ();

  // Data Transimission
  //bool send ( const std::string ) const;
  bool Send ( const char* data, const int length ) ;

  //int recv ( std::string& ) const;
  int RecvInt ( int& ) const;

  void set_non_blocking ( const bool );

  bool is_valid() const { return m_sock != -1; }

 private:

  int m_sock;
  sockaddr_in m_addr;

  bool needPID;


};


#endif
