package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layouttrace;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges.EdgeDirection;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;

public enum Layout {

	DIAGONAL_NORTH_EAST(true, false, false, true) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			for(SequenceFlowNode n : trace.getSequenceFlows()) {
				EdgeDirection edgeDirection = n.getEdgeDirection();
				if(n.isReverseSequenceFlow() || edgeDirection == EdgeDirection.BOUNDARY) {
					continue;
				}
				
				if(n.isFromSplitOrToJoin()) {
					if(!(
						edgeDirection.isNorthFacing() || 
						edgeDirection.isEastFacing()
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
	
	DIAGONAL_NORTH_WEST(true, false, true, false) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			for(SequenceFlowNode n : trace.getSequenceFlows()) {
				EdgeDirection edgeDirection = n.getEdgeDirection();
				if(n.isReverseSequenceFlow() || edgeDirection == EdgeDirection.BOUNDARY) {
					continue;
				} 
				
				if(n.isFromSplitOrToJoin()) {
					if(!(
						edgeDirection.isNorthFacing() ||
						edgeDirection.isWestFacing()
					)) {
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
	
	DIAGONAL_SOUTH_EAST(false, true, false, true) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			for(SequenceFlowNode n : trace.getSequenceFlows()) {
				EdgeDirection edgeDirection = n.getEdgeDirection();
				if(n.isReverseSequenceFlow() || edgeDirection == EdgeDirection.BOUNDARY) {
					continue;
				}
				
				if(n.isFromSplitOrToJoin()) {
					if(!(
						edgeDirection.isSouthFacing() || 
						edgeDirection.isEastFacing()
					)) {
						return false;
					}
				} else {
					if(!edgeDirection.isSouthEastFacing()) {
						return false;
					}
				}
			}
			return true;
		}
	},
	
	DIAGONAL_SOUTH_WEST(false, true, true, false) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			for(SequenceFlowNode n : trace.getSequenceFlows()) {
				EdgeDirection edgeDirection = n.getEdgeDirection();
				if(n.isReverseSequenceFlow() || edgeDirection == EdgeDirection.BOUNDARY) {
					continue;
				} 
				
				if(n.isFromSplitOrToJoin()) {
					if(!(
						edgeDirection.isSouthFacing() || 
						edgeDirection.isWestFacing()
					)) {
						return false;
					}
				} else {
					if(!edgeDirection.isSouthWestFacing()) {
						return false;
					}
				}
			}
			return true;
		}
	},
	
	
	LEFT_RIGHT_PURE(false, false, false, true) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			for(SequenceFlowNode n : trace.getSequenceFlows()) {
				EdgeDirection edgeDirection = n.getEdgeDirection();
				if(n.isReverseSequenceFlow() || edgeDirection == EdgeDirection.BOUNDARY) {
					continue;
				}
				
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
	
	LEFT_RIGHT_GATEWAY_VERTICAL_ALLOWED(false, false, false, true) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			boolean hadVerticalGatewayConncect = false;
			for(SequenceFlowNode n : trace.getSequenceFlows()) {
				EdgeDirection edgeDirection = n.getEdgeDirection();
				if(n.isReverseSequenceFlow() || edgeDirection == EdgeDirection.BOUNDARY) {
					continue;
				}
				
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
	
	LEFT_RIGHT_DIRTY(false, false, false, true) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			boolean dirtyLeft = false;
			for(SequenceFlowNode n : trace.getSequenceFlows()) {
				EdgeDirection edgeDirection = n.getEdgeDirection();
				if(n.isReverseSequenceFlow() || edgeDirection == EdgeDirection.BOUNDARY) {
					continue;
				}
				
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
	
	EVENT_SUBPROCESS(true, true, true, true) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			return false;
		}
	},
	
	OTHER(false, false, false, false) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			return false;
		}
	}, 
	
	TOP_DOWN_PURE(false, true, false, false) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			if(trace.getSequenceFlows().size() < 1) {
				return false;
			}
			
			for(SequenceFlowNode n : trace.getSequenceFlows()) {
				EdgeDirection edgeDirection = n.getEdgeDirection();
				if(n.isReverseSequenceFlow() || edgeDirection == EdgeDirection.BOUNDARY) {
					continue;
				}
				
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
	
	TOP_DOWN_GATEWAY_HORIZONTAL_ALLOWED(false, true, false, false) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			boolean hadHorizontalGatewayConnect = false;
			for(SequenceFlowNode n : trace.getSequenceFlows()) {
				EdgeDirection edgeDirection = n.getEdgeDirection();
				if(n.isReverseSequenceFlow() || edgeDirection == EdgeDirection.BOUNDARY) {
					continue;
				}
				
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
	
	TOP_DOWN_DIRTY(false, true, false, false) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			boolean dirty = false;
			for(SequenceFlowNode n : trace.getSequenceFlows()) {
				EdgeDirection edgeDirection = n.getEdgeDirection();
				if(n.isReverseSequenceFlow() || edgeDirection == EdgeDirection.BOUNDARY) {
					continue;
				}
				
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
	
	MULTILINE_EAST(false, false, false, true) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			return traceMatches(trace, "e+(xe*)*");
		}
	},
	
	MULTILINE_SOUTH(false, true, false, false) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			return traceMatches(trace, "s+(xs*)*");
		}
		
	},
	
	SNAKE_EAST(false, false, false, true) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			return traceMatches(trace, "e+(s+w+s+e+)*(s+w+(s+e*)?)");
		}
	},
	
	SNAKE_SOUTH(false, true, false, false) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			return traceMatches(trace, "s+(e+n+e+s+)*(e+n+(e+s*)?)");
		}
	}, 
	
	EMPTY(false, false, false, false) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			return false;
		}
	}, 
	
	SINGLE_ACTIVITY(true, true, true, true) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			return false;
		}
	}, 
	
	BOUNDARY_EVENT(false, false, false, false) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			return (
				trace.getSequenceFlows().size() == 1 &&
				trace.getSequenceFlows().get(0).getEdgeDirection().equals(EdgeDirection.BOUNDARY)
			);
		}
	},
	
	SOMEHOW_EAST(false, false, false, true) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			return false;
		}
	},
	
	SOMEHOW_WEST(false, false, true, false) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			return false;
		}
	},
	
	SOMEHOW_NORTH(true, false, false, false) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			return false;
		}
	},
	
	SOMEHOW_SOUTH(false, true, false, false) {
		@Override
		public boolean isLayout(SequenceFlowTrace trace) {
			return false;
		}
	},
	
	
	;
	
	private boolean isEast;
	private boolean isWest;
	private boolean isSouth;
	private boolean isNorth;

	Layout(boolean isNorth, boolean isSouth, boolean isWest, boolean isEast) {
		this.isNorth = isNorth;
		this.isSouth = isSouth;
		this.isWest = isWest;
		this.isEast = isEast;
	}
	
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
		
		return SINGLE_ACTIVITY;
	}

	private static final Layout[] LAYOUTS_BY_PRECEDENCE = new Layout[] {
			EMPTY,
			BOUNDARY_EVENT,
			DIAGONAL_NORTH_EAST,
			DIAGONAL_NORTH_WEST,
			DIAGONAL_SOUTH_EAST,
			DIAGONAL_SOUTH_WEST,
			LEFT_RIGHT_PURE,
			TOP_DOWN_PURE,
			LEFT_RIGHT_GATEWAY_VERTICAL_ALLOWED,
			TOP_DOWN_GATEWAY_HORIZONTAL_ALLOWED,
			LEFT_RIGHT_DIRTY,
			TOP_DOWN_DIRTY,
			MULTILINE_EAST,
			MULTILINE_SOUTH,
			SNAKE_EAST,
			SNAKE_SOUTH,
			EVENT_SUBPROCESS,
			OTHER,
			SINGLE_ACTIVITY,
			SOMEHOW_EAST,
			SOMEHOW_NORTH,
			SOMEHOW_SOUTH,
			SOMEHOW_WEST
		};
	
	private static Layout evaluateTraceLayout(SequenceFlowTrace trace) {
		
		if(LAYOUTS_BY_PRECEDENCE.length != values().length) {
			throw new RuntimeException("Programming Error -> please fix!");
		}
		
		for(Layout l : LAYOUTS_BY_PRECEDENCE) {
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
				case BOUNDARY:
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
	
	public boolean isEast() {
		return isEast;
	}
	
	public boolean isWest() {
		return isWest;
	}
	
	public boolean isNorth() {
		return isNorth;
	}
	
	public boolean isSouth() {
		return isSouth;
	}
}
