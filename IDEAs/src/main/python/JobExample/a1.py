#!/usr/bin/python

# Example code for extracting information from the ontology file in python 3.x 
# This will iterate through the concepts and pull out their super-concept
# Check out the ElementTree doc here: https://docs.python.org/2/library/xml.etree.elementtree.html

import xml.etree.ElementTree as ET

tree1 = ET.parse('trips-ont-dsl.xml')
root1 = tree1.getroot()  # DSL
print(root1)
for concept in root1.findall('concept'):  # find all concept nodes
    print(concept.get('name'))  # print the concept name
    for parentClass in concept.findall(
            "relation[@label='inherit']"):  # get all the `inherit` relations (i.e. super concepts) for this concept. Remove the whitespace
        print("\t", parentClass.text.strip())

tree2 = ET.parse('testCases.xml')
root2 = tree2.getroot()  # DSL
print(root2)
for tests in root2.findall('test'):
    for patterns in tests.findall('pattern'):
        # print(patterns)
        for events in patterns.findall('event'):
            print(events.get('concept)'))
