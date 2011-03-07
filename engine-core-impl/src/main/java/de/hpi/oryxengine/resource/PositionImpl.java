package de.hpi.oryxengine.resource;

import java.util.HashSet;
import java.util.Set;

import de.hpi.oryxengine.resource.OrganizationUnit;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Position;

/**
 * {@inheritDoc}
 * 
 * @author Gerardo Navarro Suarez
 */
public class PositionImpl extends ResourceImpl<Position> implements Position {

    private Participant positionHolder;
    private OrganizationUnit organizationalUnit;
    private Position superiorPosition;
    private Set<PositionImpl> subordinatePositions;

    /**
     * The Default Constructor. Creates a position object with the given id.
     * 
     * @param positionId
     *            - identifier for the position Object
     */
    public PositionImpl(String positionId) {

        super(positionId);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Participant getPositionHolder() {

        return positionHolder;
    }

    public Position setPositionHolder(Participant participant) {

        positionHolder = participant;
        return this;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Position getSuperiorPosition() {

        return superiorPosition;
    }

    public Position setSuperiorPosition(Position position) {

        superiorPosition = position;

        return this;
    }

    @Override
    public OrganizationUnit belongstoOrganization() {

        // TODO hier nochmal quatschen ob eine Position nicht doch eine Orga haben muss!!!
        return organizationalUnit;
    }

    public Position belongstoOrganization(OrganizationUnit organizationalUnit) {

        this.organizationalUnit = organizationalUnit;
        return this;
    }

    public Set<PositionImpl> getSubordinatePositionImpls() {

        if (subordinatePositions == null) {
            subordinatePositions = new HashSet<PositionImpl>();
        }
        return subordinatePositions;
    }
}