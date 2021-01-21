package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layout;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges.EdgeDirection;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;

public enum Layout {

	DIAGONAL_NORTH_EAST {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			for(SequenceFlowNode n : trace.getSequenceFlows()) {
				if(n.isReverseSequenceFlow()) {
					continue;
				} 
				
				EdgeDirection edgeDirection = n.getEdgeDirection();
				if(n.isFromSplitOrToJoin()) {
					if(!(
						edgeDirection.isNorthFacing() || 
						edgeDirection.isWestFacing()
					)) {
						return false;
					}
				} else {
					if(!edgeDirection.isNorthEastFacing()) {
						return false;
					}
				}
			}
			return true;
		}
	},
	
	DIAGONAL_NORTH_WEST {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			for(SequenceFlowNode n : trace.getSequenceFlows()) {
				if(n.isReverseSequenceFlow()) {
					continue;
				} 
				
				EdgeDirection edgeDirection = n.getEdgeDirection();
				if(n.isFromSplitOrToJoin()) {
					if(!edgeDirection.isWestFacing()) {
						return false;
					}
				} else {
					if(!edgeDirection.isNorthWestFacing()) {
						return false;
					}
				}
			}
			return true;
		}
	},
	
	PURE_LEFT_RIGHT {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			for(SequenceFlowNode n : trace.getSequenceFlows()) {
				if(n.isReverseSequenceFlow()) {
					continue;
				} 
				
				EdgeDirection edgeDirection = n.getEdgeDirection();
				if(n.isFromSplitOrToJoin()) {
					if(!edgeDirection.isEastFacing()) {
						return false;
					}
				} else {
					if(edgeDirection != EdgeDirection.DIRECT_EAST) {
						return false;
					}
				}
			}
			return true;
		}
	},
	
	LEFT_RIGHT_GATEWAY_VERTICAL_ALLOWED {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			boolean hadVerticalGatewayConncect = false;
			for(SequenceFlowNode n : trace.getSequenceFlows()) {
				if(n.isReverseSequenceFlow()) {
					continue;
				} 
				
				EdgeDirection edgeDirection = n.getEdgeDirection();
				if(n.isFromSplitOrToJoin()) {
					if(edgeDirection.isWestFacing()) {
						return false;
					}
					if(edgeDirection == EdgeDirection.DIRECT_SOUTH || edgeDirection == EdgeDirection.DIRECT_NORTH) {
						hadVerticalGatewayConncect = true;
					}
				} else {
					if(edgeDirection != EdgeDirection.DIRECT_EAST) {
						return false;
					}
				}
			}
			return hadVerticalGatewayConncect;
		}
	},
	
	DIRTY_LEFT_RIGHT {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			boolean dirtyLeft = false;
			for(SequenceFlowNode n : trace.getSequenceFlows()) {
				if(n.isReverseSequenceFlow()) {
					continue;
				} 
				
				EdgeDirection edgeDirection = n.getEdgeDirection();
				if(n.isFromSplitOrToJoin()) {
					if(edgeDirection.isWestFacing()) {
						return false;
					}
				} else {
					if(!edgeDirection.isEastFacing()) {
						return false;
					}
					if(edgeDirection != EdgeDirection.DIRECT_EAST) {
						dirtyLeft = true;
					}
				}
			}
			return dirtyLeft;
		}
	},
	
	EVENT_SUBPROCESS {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			return false;
		}
	},
	
	OTHER {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			return false;
		}
	}, 
	
	PURE_TOP_DOWN {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			if(trace.getSequenceFlows().size() < 1) {
				return false;
			}
			
			for(SequenceFlowNode n : trace.getSequenceFlows()) {
				if(n.isReverseSequenceFlow()) {
					continue;
				} 
				
				EdgeDirection edgeDirection = n.getEdgeDirection();
				if(n.isFromSplitOrToJoin()) {
					if(!edgeDirection.isSouthFacing()) {
						return false;
					}
				} else {
					if(edgeDirection != EdgeDirection.DIRECT_SOUTH) {
						return false;
					}
				}
			}
			return true;
		}
	},
	
	TOP_DOWN_GATEWAY_HORIZONTAL_ALLOWED {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			boolean hadHorizontalGatewayConnect = false;
			for(SequenceFlowNode n : trace.getSequenceFlows()) {
				if(n.isReverseSequenceFlow()) {
					continue;
				} 
				
				EdgeDirection edgeDirection = n.getEdgeDirection();
				if(n.isFromSplitOrToJoin()) {
					if(edgeDirection.isNorthFacing()) {
						return false;
					}
					if(edgeDirection == EdgeDirection.DIRECT_WEST || edgeDirection == EdgeDirection.DIRECT_EAST) {
						hadHorizontalGatewayConnect = true;
					}
				} else {
					if(edgeDirection != EdgeDirection.DIRECT_SOUTH) {
						return false;
					}
				}
			}
			return hadHorizontalGatewayConnect;
		}
	}, 
	
	DIRTY_TOP_DOWN {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			boolean dirty = false;
			for(SequenceFlowNode n : trace.getSequenceFlows()) {
				if(n.isReverseSequenceFlow()) {
					continue;
				} 
				
				EdgeDirection edgeDirection = n.getEdgeDirection();
				if(n.isFromSplitOrToJoin()) {
					if(edgeDirection.isNorthFacing()) {
						return false;
					}
				} else {
					if(!edgeDirection.isSouthFacing()) {
						return false;
					}
					if(edgeDirection != EdgeDirection.DIRECT_SOUTH) {
						dirty = true;
					}
				}
			}
			return dirty;
		}
	},
	
	MULTILINE_EAST {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			return traceMatches(trace, "e+(xe*)*");
		}
	},
	
	MULTILINE_SOUTH {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			return traceMatches(trace, "s+(xs*)*");
		}
		
	},
	
	SNAKE_EAST {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			return traceMatches(trace, "e+(s+w+s+e+)*(s+w+(s+e*)?)");
		}
	},
	
	SNAKE_SOUTH {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			return traceMatches(trace, "s+(e+n+e+s+)*(e+n+(e+s*)?)");
		}
	}
	;
	
	public abstract boolean isLayout(SequenceFlowTrace trace);

	public static Layout evaluateLayout(SequenceFlowTrace trace) {
		if(trace.getSingleFlowNode() != null) {
			return evaluateSingleFlowNodeLayout(trace);
		} else {
			return evaluateTraceLayout(trace);
		}
	}

	private static Layout evaluateSingleFlowNodeLayout(SequenceFlowTrace trace) {
		FlowNode fn = trace.getSingleFlowNode();
		if(fn.getType().equals("subProcess")) {
			return EVENT_SUBPROCESS;
		}
		
		return OTHER;
	}

	private static Layout evaluateTraceLayout(SequenceFlowTrace trace) {
		Layout[] layoutsByPrecedence = new Layout[] {
			PURE_LEFT_RIGHT,
			PURE_TOP_DOWN,
			LEFT_RIGHT_GATEWAY_VERTICAL_ALLOWED,
			TOP_DOWN_GATEWAY_HORIZONTAL_ALLOWED,
			DIRTY_LEFT_RIGHT,
			DIRTY_TOP_DOWN,
			DIAGONAL_NORTH_EAST,
			DIAGONAL_NORTH_WEST,
			MULTILINE_EAST,
			MULTILINE_SOUTH,
			SNAKE_EAST,
			SNAKE_SOUTH,
			OTHER,
			EVENT_SUBPROCESS
		};
		if(layoutsByPrecedence.length != values().length) {
			throw new RuntimeException("Programming Error -> please fix!");
		}
		
		for(Layout l : values()) {
			if(l.isLayout(trace)) {
				return l;
			}
		}
		return OTHER;
	}
	
	private static String encodeWithOutSplitsAndJoins(SequenceFlowTrace trace) {
		StringBuilder sb = new StringBuilder();
		
		for(SequenceFlowNode n : trace.getSequenceFlows()) {
			if(!n.isFromSplitOrToJoin() && !n.isReverseSequenceFlow()) {
				switch(n.getEdgeDirection()) {
				case DIRECT_EAST: 
					sb.append("e");
					break;
				case DIRECT_NORTH:
					sb.append("n");
					break;
				case DIRECT_WEST:
					sb.append("w");
					break;
				case DIRECT_SOUTH:
					sb.append("s");
					break;
				default:
					sb.append("x");
				}
			}
		}
		
		return sb.toString();
	}
	
	private static boolean traceMatches(SequenceFlowTrace trace, String regex) {
		return encodeWithOutSplitsAndJoins(trace).matches("^" + regex + "$");
	}
}