package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum EdgeDirection {
	
	DIRECT_EAST,
	DIRECT_NORTHEAST,
	DIRECT_NORTH,
	DIRECT_NORTHWEST,
	DIRECT_WEST,
	DIRECT_SOUTHWEST,
	DIRECT_SOUTH,
	DIRECT_SOUTHEAST,
	
	PERPENDICULAR_EAST,
	PERPENDICULAR_NORTHEAST,
	PERPENDICULAR_NORTH,
	PERPENDICULAR_NORTHWEST,
	PERPENDICULAR_WEST,
	PERPENDICULAR_SOUTHWEST,
	PERPENDICULAR_SOUTH,
	PERPENDICULAR_SOUTHEAST,
	
	ZAGGED_EAST,
	ZAGGED_NORTHEAST,
	ZAGGED_NORTH,
	ZAGGED_NORTHWEST,
	ZAGGED_WEST,
	ZAGGED_SOUTHWEST,
	ZAGGED_SOUTH,
	ZAGGED_SOUTHEAST,
	
	BEND_EAST,
	BEND_NORTHEAST,
	BEND_NORTH,
	BEND_NORTHWEST,
	BEND_WEST,
	BEND_SOUTHWEST,
	BEND_SOUTH,
	BEND_SOUTHEAST,
	BEND_ORIGIN;
	
	private static List<EdgeDirection> allEdgeDirectionsWith(String direction) {
		return Arrays.asList(
			EdgeDirection.values()).stream().filter(
				x -> x.name().contains(direction)  
			).collect(Collectors.toList()
		);
	}
	
	public static final List<EdgeDirection> EAST_FACING_ARCS = allEdgeDirectionsWith("EAST");
	public static final List<EdgeDirection> WEST_FACING_ARCS = allEdgeDirectionsWith("WEST");
	public static final List<EdgeDirection> SOUTH_FACING_ARCS = allEdgeDirectionsWith("SOUTH");
	public static final List<EdgeDirection> NORTH_FACING_ARCS = allEdgeDirectionsWith("NORTH");
	public static final List<EdgeDirection> NORHTWEST_FACING_ARCS = allEdgeDirectionsWith("NORTHWEST");
	public static final List<EdgeDirection> SOUTHWEST_FACING_ARCS = allEdgeDirectionsWith("SOUTHWEST");
	public static final List<EdgeDirection> NORHTEAST_FACING_ARCS = allEdgeDirectionsWith("NORTHEAST");
	public static final List<EdgeDirection> SOUTHEAST_FACING_ARCS = allEdgeDirectionsWith("SOUTHEST");
	
	public boolean isEastFacing() {
		return EAST_FACING_ARCS.contains(this);
	}
	
	public boolean isWestFacing() {
		return WEST_FACING_ARCS.contains(this);
	}
	
	public boolean isSouthFacing() {
		return SOUTH_FACING_ARCS.contains(this);
	}
	
	public boolean isNorthFacing() {
		return NORTH_FACING_ARCS.contains(this);
	}
	
	public boolean isNorthWestFacing() {
		return NORHTWEST_FACING_ARCS.contains(this);
	}

	public boolean isNorthEastFacing() {
		return NORHTEAST_FACING_ARCS.contains(this);
	}
}
