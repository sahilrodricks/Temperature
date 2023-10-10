# Temperature

The following API accepts a JSON blob in the following format:

     -{"data": __data_string__}
The data_string is a String representation of KV pairs as follows:


    `__device_id__:__epoch_ms__:'Temperature':__temperature__`
    where `__device_id__` is the device ID (int32)
    `__epoch_ms__` is the timestamp in EpochMS (int64)
    `__temperature__` is the temperature (float64)
    and `'Temperature'` is the exact string


The API has strict validations on the free form text 'Temperature' being in single quotes and 
case-sensitive. The data types for each property of the data_string must be followed to avoid 
loss of precision.

This application is hosted on an EBS instance of AWS at the following urls:

GET: http://temperatureapplication-env.eba-rggpvpmq.us-west-1.elasticbeanstalk.com/errors

DELETE: http://temperatureapplication-env.eba-rggpvpmq.us-west-1.elasticbeanstalk.com/errors

POST: http://temperatureapplication-env.eba-rggpvpmq.us-west-1.elasticbeanstalk.com/temp

POST: Any request with an improper format will be returned with a http 400 with the payload:


        {"error": "bad request"}
The request body will be stored in the application along with other incorrect data_strings. 
These strings can be accessed with the GET call, and deleted with the DELETE call.

Author: Sahil Rodricks
srodricks39@gmail.com







