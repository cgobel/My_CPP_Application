#include <jni.h>
#include <string>
#include <android/sensor.h>
#include <unistd.h>
#include <stdio.h>
#include <time.h>

const ASensor  *accelerometer ;
ASensorManager *manager;
ALooper *mylooper;
ASensorEventQueue *queue = NULL;
const int LOOPER_ID = 3;
const int SENSOR_REFRESH_RATE_HZ = 500; // rate in HZ
constexpr int32_t SENSOR_REFRESH_PERIOD_US = int32_t(1000000 / SENSOR_REFRESH_RATE_HZ);

extern "C" JNIEXPORT jstring
Java_com_example_mycppapplication_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
    JNIEXPORT jint
    Java_com_example_mycppapplication_MainActivity_enableAccelerometer(
        JNIEnv *env, jobject /* this */) {
        int status = 0;
        manager = ASensorManager_getInstance();
        if (manager == NULL) {
            return -1;
        }
        accelerometer = ASensorManager_getDefaultSensor(manager, ASENSOR_TYPE_ACCELEROMETER);
        if (accelerometer == NULL) {
            return -2;
        }
        mylooper = ALooper_prepare(ALOOPER_PREPARE_ALLOW_NON_CALLBACKS);

        if (mylooper == NULL) {
            return -3;
        }
        queue = ASensorManager_createEventQueue(manager, mylooper, LOOPER_ID, NULL, NULL);
        if (queue == NULL) {
            return -4;
        }
        status = ASensorEventQueue_registerSensor(queue, accelerometer, SENSOR_REFRESH_PERIOD_US, 0);
        if (status < 0) {
            return -5;
        }
        return 0;
    }
extern "C"
    JNIEXPORT jint
    Java_com_example_mycppapplication_MainActivity_disableAccelerometer(
            JNIEnv *env, jobject /* this */) {
        int status = 0;
        //assert(queue != NULL && accelerometer != NULL);
        ASensorEventQueue_disableSensor(queue, accelerometer);
        //assert(status >= 0);
        return 0;
    }
extern "C"
    JNIEXPORT jdoubleArray
    Java_com_example_mycppapplication_MainActivity_getSensorEvent(
            JNIEnv *env, jobject /* this */) {
        int status = 0;
        double values[3];
        ASensorEvent event;
        int count = 0;
        assert(queue != NULL && accelerometer != NULL);
        /*while(ASensorEventQueue_getEvents(queue, &event, 1) == 1){
            //clear event queue
        }*/
        jdoubleArray array= env->NewDoubleArray(3);
        //long starttime = time(NULL);
        int ret = 0;
        //while(time(NULL)-starttime < 10){ // get events for 10 seconds
            ASensorEventQueue_getEvents(queue, &event, 1);
                values[0] = event.acceleration.x;
                values[1] = event.acceleration.y;
                values[2] = event.acceleration.z;
                env->SetDoubleArrayRegion(array, 0, 3, values);
                return array;
                //array[0] = event.acceleration.x;
                //count += ret; // add number of events read

            printf("ret: %d\n", ret);
       // }

        return array; // no event available
    }



