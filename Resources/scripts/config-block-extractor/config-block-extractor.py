#!/usr/bin/python

import argparse
import xml.etree.ElementTree as ET
import copy
import os
import sys
from typing import List

def init_argparse() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(description='Extracts single configuration blocks from all exported NEPO program XMLs in a directory.')
    parser.add_argument('input_dir', metavar='input_dir', type=str, nargs=1)
    return parser

def process_xml_file(file_path: str) -> List[ET.Element]:
    tree = ET.parse(file_path)
    root = tree.getroot()
    output_block_sets = []
    namespaces = {'blockly': 'http://de.fhg.iais.roberta.blockly'}

    config_block_set = root.find('blockly:config', namespaces).find('blockly:block_set', namespaces)
    for instance in config_block_set.findall('blockly:instance', namespaces):
        output_builder = ET.TreeBuilder()
        output_builder.start('block_set', config_block_set.attrib)
        output_instance = output_builder.start('instance')
        output_instance.attrib = instance.attrib
        output_instance.append(instance.find('blockly:block', namespaces))
        output_instance = copy.deepcopy(instance)
        output_builder.end('instance')
        output_builder.end('block_set')
        output_block_sets.append(output_builder.close())
    
    return output_block_sets

def save_extracted_xmls(file_path: str, output_block_sets : List[ET.Element]) -> None:
    output_dir_name = file_path[:-4] # .xml ending is assumed

    if not os.path.exists(output_dir_name):
        os.mkdir(output_dir_name)

    # Remove namespace from output xml
    ET.register_namespace('', 'http://de.fhg.iais.roberta.blockly')
    for block_set in output_block_sets:
        output_tree = ET.ElementTree(block_set)

        output_tree.write(os.path.join(output_dir_name, block_set[0][0].attrib['type'] + '.xml'))

def main() -> None:
    parser = init_argparse()
    args = parser.parse_args()
    
    input_dir = args.input_dir[0]

    if not os.path.isdir(input_dir):
        sys.exit(input_dir + ' is not a directory!')

    for file_name in os.listdir(input_dir):
        if file_name.endswith('.xml'):
            output_block_sets = process_xml_file(os.path.join(input_dir, file_name))
            save_extracted_xmls(os.path.join(input_dir, file_name), output_block_sets)

if __name__ == "__main__":
    main()
