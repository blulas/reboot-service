Ansible playbooks
=========

Included are Ansible playbooks for building, deploying, and testing the Intelli-Infra container.

Requirements
------------

To use the playbooks you must have Ansible 2.9 or greater installed.

Variables
--------------

* vars/build_vars.yml
  * local_repo_dir:  directory name (not path) on podman / maven host to clone the git repository.  Will be created in the ansible user's home directory
  * remote_repo_url:  repository url to pull the reboot-service code from before building the images
  * ssh_private_key:  Specify a path on the filesystem of the podman host to an SSH private key that can be used to clone the git resository
  * quay_username:  Specify the username to login to quay.io
  * quay_password:  Specify the password, preferably encrypted, to login to quay.io

* vars/ha_vars.yml
  * podman_host:  Specify the hostname of your podman host.  This is used for API calls.
  * primary_pg_host: Currently set to read hostname from the inventory file.  Used to configure the db in HA.
  * postgresql_user: Specify any username you like.  This will be used when the container is first deployed
  * postgresql_user_password:   Password for the above user
  * postgresql_port: 5432 is the default port.  Do not change
  * springboot_port: 8080 is the default port.  Do not change
  * repl_user: Specify any username you like. User is created in the database. Used for replication of data between database hosts.
  * repl_user_password: Password for the above user
  * lb_vip_hostname: hostame used for the HA database.  Used to point the springboot app to the database.
  * mount_path:  Specify a path on the filesystem of your podman hosts where you want the DB container to mount a volume
  * failover_trigger_filename: Currently set to the filename "failover".  This is a blank file that will be written to the standby podman host in the event the primary DB host is offline and the database needs to failover to the standby.
  * reboot_minutes: Specify how many minutes to wait before a host can be rebooted again.
  * reboot_count:   Specify how many times a host may be rebooted within {{ reboot_minutes }}
  * api_body_file: A json file to be used for the API body.  This defaults to an example json file in the json directory.

Dependencies
------------

A list of other roles hosted on Galaxy should go here, plus any details in regards to parameters that may need to be set for other roles, or variables that are used from other roles.

Playbook Usage
----------------
Before you can use the playbooks you first need to make sure you update the inventory file.

**To build a new podman image:**

```
ansible-playbook build_images.yml -i inventory --ask-become-pass
```

**To download the new image and start new containers:**
In order to deploy the containers on a podman host you must have sudo access to open the necessary firewall ports, install necessary packages, configure the database in HA, etc.  The ` --ask-become-pass ` will prompt you for your sudo password.

To deploy Decision Manager as a standalone solution (non-HA database):
```
ansible-playbook deploy_dm_standalone.yml -i inventory --ask-become-pass
```

or to deploy Decision Manager with an HA database:
```
ansible-playbook deploy_dm_ha.yml -i inventory --ask-become-pass
```

**To restart the containers if stopped:**

```
ansible-playbook restart_dm.yml -i inventory --ask-become-pass
```

**To update the reboot rules:**

```
ansible-playbook update_reboot_rules.yml
```

**To test the reboot rules:**

```
ansible-playbook test_dm_api.yml
```

To view the reboot rules, host summary, and reboot details, browse to your podman host using your browser:
http://<podman hostname>:8080/service/host/


License
-------

BSD

Author Information
------------------

An optional section for the role authors to include contact information, or a website (HTML is not allowed).