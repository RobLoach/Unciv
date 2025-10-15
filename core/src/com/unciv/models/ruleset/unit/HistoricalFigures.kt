package com.unciv.models.ruleset.unit

import com.unciv.models.ruleset.Ruleset
import com.unciv.models.ruleset.RulesetObject
import com.unciv.models.ruleset.unique.UniqueTarget
import com.unciv.models.ruleset.unique.UniqueType
import com.unciv.models.translations.tr
import com.unciv.ui.components.extensions.colorFromRGB
import com.unciv.ui.objectdescriptions.uniquesToCivilopediaTextLines
import com.unciv.ui.objectdescriptions.uniquesToDescription
import com.unciv.ui.screens.civilopediascreen.FormattedLine
import com.unciv.ui.screens.pickerscreens.PromotionPickerScreen

class HistoricalFigures : RulesetObject() {
    /** A list of figure names available for this historical figure group. */
    var figures = ArrayList<String>()

    fun clone(): HistoricalFigures {
        val newHistoricalFigures = HistoricalFigures()

        // RulesetObject fields
        newHistoricalFigures.name = name
        newHistoricalFigures.uniques = uniques

        // HistoricalFigures fields
        newHistoricalFigures.figures = figures
        return newHistoricalFigures
    }

    /**
     * Retrieve a list of units that match this historical figure instance.
     */
    fun getUnits(ruleset: Ruleset) = ruleset.units.values.filter { unit ->
        unit.getMatchingUniques(UniqueType.CanBeAHistoricalFigure).any { unique ->
            // Match by using either the direct name, or a tag
            unique.params[0] == name || hasTagUnique(unique.params[0])
        }
    }

    override fun getUniqueTarget() = UniqueTarget.UnitTriggerable
    override fun makeLink() = "Historical Figures/$name"
    override fun getCivilopediaTextLines(ruleset: Ruleset): List<FormattedLine> {
        val lines = ArrayList<FormattedLine>()
        uniquesToCivilopediaTextLines(lines)

        // Units
        val units = getUnits(ruleset)
        if (units.isNotEmpty()) {
            lines.add(FormattedLine("Units", header = 4))
            for (unit in units) {
                lines.add(FormattedLine(unit.name, link = "Unit/${unit.name}"))
            }
        }

        // Figures
        if (figures.isNotEmpty()) {
            lines.add(FormattedLine())
            lines.add(FormattedLine("Historical Figures", header = 4))
            for (figure in figures) {   
                lines.add(FormattedLine(figure))
            }
        }

        return lines
    }
}
