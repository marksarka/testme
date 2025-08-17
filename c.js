// Load the AWS SDK for Node.js
const AWS = require('aws-sdk');

// Set the region (example: us-west-2)
AWS.config.update({region: 'us-west-2'});

// Create S3 service object
const s3 = new AWS.S3({
  apiVersion: '2006-03-01',
  accessKeyId: 'AKIAIOSFODNN7EXAMPLE', 
  secretAccessKey: 'wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY'
});

// Create the parameters for calling listObjects
var bucketParams = {
  Bucket : 'examplebucket', // Name of the bucket you want to access
};

// Call S3 to obtain a list of the objects in the bucket
s3.listObjects(bucketParams, function(err, data) {
  if (err) {
    console.log("Error", err);
  } else {
    console.log("Success", data);
  }
});
