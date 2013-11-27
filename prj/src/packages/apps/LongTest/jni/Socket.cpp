// Implementation of the Socket class.


#include "Socket.h"
#include <string.h>
#include <errno.h>
#include <fcntl.h>

#include <netinet/in.h>    // for sockaddr_in
#include <sys/types.h>    // for socket
#include <sys/socket.h>    // for socket
#include <stdio.h>        // for printf
#include <stdlib.h>        // for exit
#include <string.h>        // for bzero
#include <unistd.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#include <sys/un.h>
#define UNIX_PATH_MAX 108



Socket::Socket() :
	m_sock ( -1 )
{

	memset ( &m_addr,
			0,
			sizeof ( m_addr ) );

}

Socket::~Socket()
{
	if ( is_valid() )
		::close ( m_sock );
}

bool Socket::create()
{
	m_sock = socket ( AF_INET,
			SOCK_STREAM,
			0 );

	if ( ! is_valid() )
		return false;


	// TIME_WAIT - argh
	int on = 1;
	if ( setsockopt ( m_sock, SOL_SOCKET, SO_REUSEADDR, ( const char* ) &on, sizeof ( on ) ) == -1 )
		return false;


	return true;

}



bool Socket::bind ( const int port )
{

	if ( ! is_valid() )
	{
		return false;
	}



	m_addr.sin_family = AF_INET;
	m_addr.sin_addr.s_addr = INADDR_ANY;
	m_addr.sin_port = htons ( port );

	int bind_return = ::bind ( m_sock,
			( struct sockaddr * ) &m_addr,
			sizeof ( m_addr ) );


	if ( bind_return == -1 )
	{
		return false;
	}

	return true;
}


bool Socket::listen() const
{
	if ( ! is_valid() )
	{
		return false;
	}

	int listen_return = ::listen ( m_sock, MAXCONNECTIONS );


	if ( listen_return == -1 )
	{
		return false;
	}

	return true;
}


bool Socket::accept ( Socket& new_socket ) const
{
	int addr_length = sizeof ( m_addr );
	new_socket.m_sock = ::accept ( m_sock, ( sockaddr * ) &m_addr, ( socklen_t * ) &addr_length );

	if ( new_socket.m_sock <= 0 )
		return false;
	else
		return true;
}

bool Socket::Send ( const char* s, int length ) const
{
	int status = ::send ( m_sock, s, length, MSG_NOSIGNAL );
	if ( status == -1 )
	{
		return false;
	}
	else
	{
		return true;
	}
}

/*bool Socket::send ( const std::string s ) const
  {
  int status = ::send ( m_sock, s.c_str(), s.size(), MSG_NOSIGNAL );
  if ( status == -1 )
  {
  return false;
  }
  else
  {
  return true;
  }
  }*/

/*
   int Socket::recv ( std::string& s ) const
   {
   char buf [ MAXRECV + 1 ];

   s = "";

   memset ( buf, 0, MAXRECV + 1 );

   int status = ::recv ( m_sock, buf, MAXRECV, 0 );

   if ( status == -1 )
   {
//     std::cout << "status == -1   errno == " << errno << "  in Socket::recv\n";
return 0;
}
else if ( status == 0 )
{
return 0;
}
else
{
s = buf;
return status;
}
}
*/

bool Socket::Connect ( const char* host, const int port )
{
#ifndef DEBUG_MODE
	struct sockaddr_un address;

	m_sock = socket(PF_UNIX, SOCK_STREAM, 0); 
	if(m_sock < 0)
	{   
		//    ALOG(LOG_INFO,"HAIYANG","CL: Create Socket Failed! %d",errno);
		return false; 
	}   

	// start with a clean address structure 
	memset(&address, 0, sizeof(struct sockaddr_un));

	address.sun_family = AF_UNIX;
	snprintf(address.sun_path, UNIX_PATH_MAX, "/dev/socket/instrument");

	if(connect(m_sock, 
				(struct sockaddr *) &address,  
				sizeof(struct sockaddr_un)) != 0)
	{   
		//        ALOG(LOG_INFO,"HAIYANG","CL: Connect Socket Failed! %d",errno);
		//        m_sock = -1; 
		return false;
	}   
	return true;
#else
	printf("in debug mode \n");
	m_sock = socket ( AF_INET,
			SOCK_STREAM,
			0 );
	if ( ! is_valid() ) return false;

	m_addr.sin_family = AF_INET;
	m_addr.sin_port = htons ( port );

	int status = inet_pton ( AF_INET, host, &m_addr.sin_addr );

	if ( errno == EAFNOSUPPORT ) return false;

	status = ::connect ( m_sock, ( sockaddr * ) &m_addr, sizeof ( m_addr ) );

	if ( status == 0 )
		return true;
	else
		return false;

#endif
}
/*
   bool Socket::connect ( const std::string host, const int port )
   {
   if ( ! is_valid() ) return false;

   m_addr.sin_family = AF_INET;
   m_addr.sin_port = htons ( port );

   int status = inet_pton ( AF_INET, host.c_str(), &m_addr.sin_addr );

   if ( errno == EAFNOSUPPORT ) return false;

   status = ::connect ( m_sock, ( sockaddr * ) &m_addr, sizeof ( m_addr ) );

   if ( status == 0 )
   return true;
   else
   return false;
   }
   */
void Socket::set_non_blocking ( const bool b )
{

	int opts;

	opts = fcntl ( m_sock,
			F_GETFL );

	if ( opts < 0 )
	{
		return;
	}

	if ( b )
		opts = ( opts | O_NONBLOCK );
	else
		opts = ( opts & ~O_NONBLOCK );

	fcntl ( m_sock,
			F_SETFL,opts );

}