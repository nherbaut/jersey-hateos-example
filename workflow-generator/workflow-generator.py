#!/usr/bin/env python3

import json
import matplotlib.pyplot as plt
from lxml import etree
import copy
import networkx as nx
import re
import os
import random

IP=os.environ.get("IP",None)
if IP is None:
    print("please specify env var IP")
    exit(-1)
else:
    if ":" in IP:
        IP=IP.split(":")
    else:
        IP=[IP]
class Payload:
    def __init__(self, instructions=0, in_bytes_count=0, out_bytes_count=0, dummy_padding=0):
        self.instructions = instructions
        self.in_bytes_count = in_bytes_count
        self.out_bytes_count = out_bytes_count
        self.dummy_padding = dummy_padding


def get_tag_type(element):
    return re.findall("\{.*\}(.*)", element.tag)[0]


def get_random_payload(tag_type):
    return vars(Payload())


def visit_bpmn(father, nodes, g):
    for node in nodes:
        if node not in g:
            node_name = node.get("id")
            tag_type = get_tag_type(node)
            if (tag_type.endswith("Gateway")):
                g.add_node(node_name, type=tag_type, gatewayDirection=node.get("gatewayDirection"),
                           payload=get_random_payload(tag_type))
            else:
                g.add_node(node_name, type=get_tag_type(node), payload=get_random_payload(tag_type))
            g.add_edge(father, node_name)
            edge_names = node.xpath("bpmn2:outgoing/text()", namespaces=ns)
            for edge_name in edge_names:
                next_tasks = doc.xpath("//bpmn2:incoming[. = '" + edge_name + "']/..", namespaces=ns)
                visit_bpmn(node_name, next_tasks, g)




if __name__=='__main__':
    root = etree.XML(open("example.xml", "r").read().encode("ascii", "ignore"))
    doc = etree.ElementTree(root)
    ns = {'bpmn2': 'http://www.omg.org/spec/BPMN/20100524/MODEL', }

    g = nx.DiGraph()

    start_events = doc.xpath("//bpmn2:startEvent", namespaces=ns)

    g.add_node("START", type="START", payload=get_random_payload("START"))
    visit_bpmn("START", start_events, g)
    # nx.draw(g)
    # nx.draw_networkx_labels(g,pos=nx.spring_layout(g))
    # plt.draw()
    i=8080
    for n in g.nodes:
        g.nodes[n]["url"]="http://%s:%d"%(random.choice(IP),i)
        #i+=1

    print(json.dumps(nx.node_link_data(g)))
