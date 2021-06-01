from datetime import datetime
from typing import Dict

from scipy.interpolate import interp1d


def give_args():
    names = []
    times = []
    keys = []

    names.append("HeadPitch")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 34.4, 37, 39.6, 42.2, 44.4, 46.2, 50])
    keys.append([0, 8.95233e-08, -4.76838e-07, 8.89455e-08, 1.04976e-07, 0.331613, 0.314159, 9.19019e-08, -0.331613, 0.139626, -
    0.0872665, 0.139626, 0.383972, 0.558505, 0.383972, -0.331613, 0.139626, -0.0872665, 0.139626, 0.383972, 0, -0.190258])

    names.append("HeadYaw")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 34.4, 37, 39.6, 42.2, 44.4, 46.2, 50])
    keys.append([0, 8.42936e-08, 8.42938e-08, 8.42938e-08, -4.76838e-07, 0.314159, -0.296706, -1.18682, -0.279253, 0.20944,
                 1.5708, 0.20944, 0.139626, 0, -0.139626, 0.279253, -0.20944, -1.5708, -0.20944, -0.139626, 0, -0.00310993])

    names.append("LAnklePitch")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 34.4, 37, 39.6, 42.2, 43.4, 44.4, 46.2, 50])
    keys.append([1.00403e-07, 0, -0.303687, 0, 0, -0.647517, -0.610865, -1.0472, -1.0472, -1.0472, -1.0472, -1.0472, -
    1.0472, -0.872665, -0.741765, 0, 1.00403e-07, 0.523599, 1.00403e-07, -0.555015, -0.654498, -1.0472, 0.033706])

    names.append("LAnkleRoll")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 33.4, 34.4, 37, 39.6, 42.2, 44.4, 46.2, 50])
    keys.append([0.0523599, 0.122173, 0.174533, -0.10472, -0.10472, 0.174533, -0.261799, 0.0628318, 0.1309,
                 0, 0, 0, 0.0872665, 0, -0.240855, -0.55676, -0.424115, -0.349066, 0, -0.349066, -0.312414, 0, -0.05058])

    names.append("LElbowRoll")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 34.4, 37, 39.6, 42.2, 44.4, 45.4, 46.2, 50])
    keys.append([0, -0.698132, -1.0472, 0, 0, -1.65806, -0.959931, -1.48353, -1.01229, -1.01229, 0, -1.01229, -
    1.01229, -0.890118, -0.855211, -1.11701, -0.855211, -1.25664, -0.855211, -0.855211, -0.994838, -1.4207, -0.38806])

    names.append("LElbowYaw")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 34.4, 37, 39.6, 42.2, 44.4, 45.4, 46.2, 50])
    keys.append([-1.5708, -1.5708, -1.5708, -1.5708, -1.5708, -0.383972, 0, 0, 0, 0, 0, 0, 0, 0.20944,
                 0.191986, -0.418879, -0.418879, -0.0872665, -0.418879, 0.191986, -0.378736, -0.244346, -1.18276])

    names.append("LHand")
    times.append([3, 50])
    keys.append([0, 0.2984])

    names.append("LHipPitch")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 34.4, 37, 39.6, 42.2, 44.4, 46.2, 50])
    keys.append([0, 0, -0.349066, 0, 0, -0.698132, -0.610865, -1.0472, -1.0472, -1.0472, -1.0472, -1.0472, -
    1.0472, -0.872665, -0.741765, -0.122173, -0.872665, 0, -0.872665, -0.654498, -1.0472, 0.216335])

    names.append("LHipRoll")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 33.4, 34.4, 37, 39.6, 42.2, 44.4, 46.2, 50])
    keys.append([-0.0523599, -0.122173, -0.174533, 0.10472, 0.10472, -0.174533, 0.174533, 0.420624, 0.528835, 0.610865,
                 0.610865, 0.610865, 0.349066, 0, -0.261799, 0.251327, 0.261799, 0.139626, 0.698132, 0.139626, -0.261799, 0, 0.0414601])

    names.append("LHipYawPitch")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 34.4, 37, 39.6, 42.2, 44.4, 46.2, 50])
    keys.append([-0.10821, -0.120428, -0.1309, -0.120428, -0.143117, -0.167552, -
    0.0994838, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.0680678, 0, -0.194775])

    names.append("LKneePitch")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 34.4, 37, 39.6, 42.2, 44.4, 46.2, 50])
    keys.append([0, 0, 0.698132, -9.9341e-08, -9.9341e-08, 1.39626, 1.22173, 2.0944, 2.0944, 2.0944,
                 2.0944, 2.0944, 2.1101, 1.74533, 1.48353, 0.122173, 1.74533, 0, 1.74533, 1.309, 2.0944, -0.0890141])

    names.append("LShoulderPitch")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 34.4, 37, 39.6, 42.2, 44.4, 46.2, 50])
    keys.append([1.5708, 1.91986, 2.0944, 1.5708, 0, 0.366519, 0.349066, 0.191986, -0.802851, -0.174533, -0.296706, -
    0.174533, 0.523599, 0.471239, 0.331613, -0.471239, 0.0698132, -0.0698132, 0.0698132, 0.331613, 1.69297, 1.52936])

    names.append("LShoulderRoll")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 34.4, 37, 39.6, 42.2, 44.4, 46.2, 50])
    keys.append([0.174533, 0.349066, 0.174533, 0.174533, 0.174533, 0.698132, 0, 0.0872665, 0.174533,
                 0.401426, 1.15192, 0.401426, 0.401426, 0.174533, 0, 0.401426, 0, 0, 0, 0.20944, 0.942478, 0.107338])

    names.append("LWristYaw")
    times.append([3, 50])
    keys.append([-1.53589, 0.139552])

    names.append("RAnklePitch")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 34.4, 37, 39.6, 42.2, 44.4, 46.2, 50])
    keys.append([1.00403e-07, 0, 0, 0, 0, -0.698132, -0.174533, 0, 0, 1.00403e-07, 0.523599, 1.00403e-07, -
    0.741765, -0.872665, -1.0472, -1.0472, -1.0472, -1.0472, -1.0472, -1.0472, -1.0472, 0.036858])

    names.append("RAnkleRoll")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 34.4, 37, 39.6, 42.2, 44.4, 46.2, 50])
    keys.append([-0.0523599, 0.1309, 0.438078, 0.10472, 0.10472, 0.294961, 0.621337, 0.785398, 0.74351, 0.436332, 0,
                 0.349066, 0.261799, 0, -0.174533, -0.174533, -0.0424667, -0.0225556, -0.0130542, -0.00206581, 0, 0.0291878])

    names.append("RElbowRoll")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 34.4, 37, 39.6, 42.2, 44.4, 45.4, 46.2, 50])
    keys.append([0, 0.698132, 1.0472, 2.57424e-07, 0, 1.23918, 1.64061, 0.0698132, 1.11701, 0.855211, 1.25664, 0.855211,
                 0.855211, 0.890118, 1.01229, 1.01229, 1.01229, 0.0349066, 1.01229, 1.01229, 1.13272, 1.36659, 0.395814])

    names.append("RElbowYaw")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 34.4, 37, 39.6, 42.2, 44.4, 45.4, 46.2, 50])
    keys.append([1.5708, 1.5708, 1.5708, 1.5708, 1.5708, 0.191986, 0.349066, 1.5708, 0.418879, 0.418879,
                 0.0872665, 0.418879, -0.191986, -0.20944, 0, 0, 0, 0, 0, 0, 0.342085, 0.244346, 1.15966])

    names.append("RHand")
    times.append([3, 50])
    keys.append([0, 0.302])

    names.append("RHipPitch")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 34.4, 37, 39.6, 42.2, 44.4, 46.2, 50])
    keys.append([0, 0, 0, 0, 0, -0.698132, -0.174533, -0.10472, -0.122173, -0.872665, 0, -0.872665, -
    0.741765, -0.872665, -1.0472, -1.0472, -1.0472, -1.0472, -1.0472, -1.0472, -1.0472, 0.214717])

    names.append("RHipRoll")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 34.4, 37, 39.6, 42.2, 44.4, 46.2, 50])
    keys.append([0.0523599, -0.122173, -0.438078, -0.10472, -0.10472, -0.349066, -0.785398, -0.541052, -0.139626, -0.139626, -
    0.698132, -0.139626, 0.261799, 0, -0.349066, -0.539307, -0.610865, -0.610865, -0.610865, -0.532325, 0, -0.021434])

    names.append("RHipYawPitch")
    times.append([3, 5, 7, 9, 11, 13, 50])
    keys.append([-0.10821, -0.120428, -0.1309, -
    0.120428, -0.143117, -0.167552, -0.194775])

    names.append("RKneePitch")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 34.4, 37, 39.6, 42.2, 44.4, 46.2, 50])
    keys.append([0, 0, 0, 0, 0, 1.39626, 0.349066, 0.122173, 0.122173, 1.74533, 0, 1.74533,
                 1.48353, 1.74533, 2.0944, 2.0944, 2.0944, 2.0944, 2.0944, 2.0944, 2.0944, -0.091998])

    names.append("RShoulderPitch")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 34.4, 37, 39.6, 42.2, 44.4, 46.2, 50])
    keys.append([1.5708, 1.91986, 2.0944, 1.5708, 0, 0.174533, 0.610865, 1.0472, -0.471239, 0.0698132, -0.0698132,
                 0.0698132, 0.331613, 0.471239, 0.523599, -0.802851, -0.174533, -0.296706, -0.174533, 0.523599, 1.69297, 1.51563])

    names.append("RShoulderRoll")
    times.append([3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23.6, 26.2,
                  28.4, 30.4, 32.4, 34.4, 37, 39.6, 42.2, 44.4, 46.2, 50])
    keys.append([-0.174533, -0.174533, -0.349066, -0.174533, -0.174515, -0.0698132, -0.837758, -1.51844, -0.401426,
                 0, 0, 0, 0, -0.174533, -0.401426, -0.174533, -0.401426, -1.15192, -0.401426, -0.558505, -0.942478, -0.099752])

    names.append("RWristYaw")
    times.append([3, 50])
    keys.append([1.53589, 0.164096])

    return names, times, keys


MotionInformation = Dict[str, Dict[float, float]]


def get_header(names):
    line = "#WEBOTS_MOTION,V1.0,"
    line += ','.join(names)
    return line


def format_time(s):
    return datetime.fromtimestamp(s - 3600).strftime('%M:%S:%f')[:-3]


def write_lines(file, times, keys):
    times = times[0]
    for index in range(len(times)):
        time = format_time(times[index])
        file.write(time)
        for key in keys:
            print(len(key))
            file.write(",")
            file.write(str(key[index]))
        file.write("\n")


def interpolate_missing_time_stamps(time_stamps, motion_information: MotionInformation):
    new_motion_information = dict()

    for (name, value_map) in motion_information.items():
        function = interp1d(list(value_map.keys()), list(value_map.values()), fill_value="extrapolate")

        new_motion_information[name] = {time_stamp: float(function(time_stamp)) for time_stamp in time_stamps}

    return new_motion_information


def parse_motion_information(names, times_array, values_array):
    motion_information = dict()
    for (name, times, values) in zip(names, times_array, values_array):
        motion_information[name] = {time: value for (time, value) in zip(times, values)}
    return motion_information


def write_to_motion_file(filename, motion_information: MotionInformation):
    with open(filename, 'w') as file:
        file.write(get_header(motion_information.keys()) + "\n")
        pose_number = 1

        value_maps = list(motion_information.values())

        if len(value_maps) <= 0:
            return

        time_stamps = value_maps[0].keys()

        for time_stamp in time_stamps:
            line = f'{format_time(time_stamp)},Pose{pose_number},'
            line += ','.join([str(round(values_map[time_stamp], 3)) for values_map in motion_information.values()])
            file.write(line + "\n")
            pose_number += 1


def replace_hand_joints(motion_information: MotionInformation):
    new_motion_information = dict()

    for (name, values_map) in motion_information.items():
        if name == "LHand":
            for i in range(1, 9):
                new_motion_information[f'LPhalanx{i}'] = values_map
            continue
        if name == "RHand":
            for i in range(1, 9):
                new_motion_information[f'RPhalanx{i}'] = values_map
            continue

        new_motion_information[name] = values_map

    return new_motion_information


def increase_by_speed(motion_information: MotionInformation, speed):
    return {
        name: {
            time / speed: value for (time, value) in values_map.items()
        }
        for (name, values_map) in motion_information.items()
    }


def generate_motion_file(filename):
    (names, times_array, values_array) = give_args()
    time_stamps = join_time_stamps(times_array)
    motion_information = parse_motion_information(names, times_array, values_array)

    motion_information = interpolate_missing_time_stamps(time_stamps, motion_information)

    motion_information = replace_hand_joints(motion_information)
    motion_information = increase_by_speed(motion_information, speed=3)

    write_to_motion_file(filename, motion_information)


def join_time_stamps(times_array):
    time_stamps = set()
    for times in times_array:
        for time_stamp in times:
            time_stamps.add(time_stamp)

    return sorted(list(time_stamps))


if __name__ == '__main__':
    generate_motion_file("../motions/Taichi.motion")
