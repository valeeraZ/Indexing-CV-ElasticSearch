# Description
This is a project for using Elastic Search with Spring Boot to index CVs in the course "Sorbonne Université Master 2 Cours MU5IN552: Développement des Algorithmes d’Application Réticulaire."

# Team
* Wenzhuo ZHAO[(wenzhuo.zhao@etu.sorbonne-universite.fr)](mailto:wenzhuo.zhao@etu.sorbonne-universite.fr)
* Chengyu YANG[(cheng-yu.yang@etu.sorbonne-universite.fr)](mailto:cheng-yu.yang@etu.sorbonne-universite.fr)
* Zhaojie LU[(zhaojie.lu@etu.sorbonne-universite.fr)](mailto:zhaojie.lu@etu.sorbonne-universite.fr)
* Zhen HOU[(zhen.hou@etu.sorbonne-universite.fr)](mailto:zhen.hou@etu.sorbonne-universite.fr)

# Demo
We have created a front end using the APIs of POST and GET, here is a video of demo:

https://user-images.githubusercontent.com/39196828/139540517-13cf6df1-dbda-45ee-b657-121e3d07fe32.mov



# Documentation of API
See https://documenter.getpostman.com/view/10263827/UV5ZBGVr for exemples.

# Usage
1. Clone this project
2. Create your own `application.properties` in `src/main/resources`
    ```
    spring.elasticsearch.rest.uris=<ip:9200> # by default is localhost:9200
    spring.elasticsearch.rest.username=your-username
    spring.elasticsearch.rest.password=your-password
    ```
   
3. (Optional)If your Elastic Search is hosted on a remote VPS and not accessible from Internet, you might need to configure SSH connection in this project.
    * append the script to your `application.properties`:
        ```
        ssh.enabled=true
        ssh.host=<ip-vps>
        ssh.port=22
        ssh.username=<username>
        ssh.privatekey=<path/to/private-key>
        ```
    * If your private key begins with `BEGIN OPENSSH PRIVATE KEY` which is not supported by JSch, you might need to convert the key to RSA format.
        * copy your private key
            ```
            cp id_rsa id_rsa-new
            ```
        * convert the new private key
            ```
            ssh-keygen -p -f id_rsa-new -m pem 
            ```
        * then modify the path to private key...
    
4. Install "ingest attachment plugin" on every node of your Elastic Search Cluster, see https://www.elastic.co/guide/en/elasticsearch/plugins/current/ingest-attachment.html

5. Using the Attachment Processor in a Pipeline
   ```
    PUT _ingest/pipeline/attachment
    {
        "description" : "Extract attachment information",
        "processors" : [
            {
                "attachment" : {
                    "field" : "data"
                }
            }
        ]
    }
   ```
6. Once the "attachment" pipeline is set, run the project then you can try to add or search some CV files by the API in the documentation.

7. If you have Logstash installed on your machine, you can modify the IP address and port in `logback-spring.xml` by replacing them with yours and discover the logs in Kibana
  <img width="1440" alt="Screenshot 2021-10-30 at 17 49 17" src="https://user-images.githubusercontent.com/39196828/139540305-fcf4fd11-8bf0-4e3e-aa98-c68fe45607e6.png">
    
