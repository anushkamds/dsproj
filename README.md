DSProj
=========

The project development team:
  * Hirantha Sankalpa - hiranthasankalpa@gmail.com
  * Anushka Mahesh -

Protocol
========

 * Register
    length REG IP_address port_no username
    length REGOK no_nodes IP_1 port_1 IP_2 port_2

    no_ nodes – Number of node entries that are going to be returned by the registry
        0 – request is successful, no nodes in the system
        1 or 2 – request is successful, 1 or 2 nodes' contacts will be returned
        9999 – failed, there is some error in the command
        9998 – failed, already registered to you, unregister first
        9997 – failed, registered to another user, try a different IP and port
        9996 – failed, can’t register. BS full

 * Unregister
    length UNREG IP_address port_no username
    length UNROK value

    value – Indicate success or failure
        0 – successful
        9999 – error while unregistering. IP and port may not be in the registry or command is incorrect

 * Join
    length JOIN priority IP_address port_no
    length JOINOK value

    value – Indicate success or failure
        0 – successful
        9998 – new node already added to routing table
        9999 – error while adding new node to routing table

 * Leave
    length LEAVE IP_address port_no
    length LEAVEOK value

    value – Indicate success or failure
            0 – successful
            9999 – error while removing node from routing table

 * Search
    length SER IP port file_name hops
    length SEROK no_files IP port hops filename1 filename2 ... ...

    no_files – Number of results returned
        ≥ 1 – Successful
        0 – no matching results. Searched key is not in key table
        9999 – failure due to node unreachable
        9998 – some other error

 * Error
    length ERROR

 * Example
    0047 SER 129.82.62.142 5070 "Lord of the rings"
    7776 JOIN 1 127.0.0.1 7777
    7777 DISCON